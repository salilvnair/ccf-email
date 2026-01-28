package com.github.salilvnair.ccf.email.letter.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HtmlToJsonCompiler {

    public boolean isHtml(String body) {
        if (body == null || body.isBlank()) return false;

        Document doc = Jsoup.parse(body);
        return !doc.select("p,div,span,table,ul,ol,br,style,html,body").isEmpty();
    }

    private static final Set<String> ALLOWED_TAGS = Set.of(
            "p", "span",
            "b", "strong",
            "i", "em",
            "ul", "ol", "li",
            "table", "tr", "td", "th",
            "br"
    );

    public Map<String, Object> compile(String html) {
        Document doc = Jsoup.parseBodyFragment(html);

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("type", "root");

        List<Object> children = new ArrayList<>();
        for (Node n : doc.body().childNodes()) {
            Object c = compileNode(n);
            if (c != null) children.add(c);
        }

        root.put("children", children);
        return root;
    }

    private Map<String, Object> textNode(String text) {
        if (text == null || text.isBlank()) return null;
        Map<String, Object> t = new LinkedHashMap<>();
        t.put("type", "text");
        t.put("text", text);
        return t;
    }

    private Object compileNode(Node node) {

    // ---------- TEXT NODE ----------
        if (node instanceof TextNode tn) {

            String text = tn.text();
            if (text.isBlank()) return null;

            List<Object> parts = new ArrayList<>();

            int idx = 0;
            while (idx < text.length()) {

                int start = text.indexOf("[[${", idx);
                if (start < 0) {
                    // remaining plain text
                    parts.add(textNode(text.substring(idx)));
                    break;
                }

                // plain text before binding
                if (start > idx) {
                    parts.add(textNode(text.substring(idx, start)));
                }

                int end = text.indexOf("}]]", start);
                if (end < 0) {
                    // malformed, treat rest as text
                    parts.add(textNode(text.substring(start)));
                    break;
                }

                String key =
                        text.substring(start + 4, end).trim();

                Map<String, Object> bind = new LinkedHashMap<>();
                bind.put("type", "binding");
                bind.put("key", key);

                parts.add(bind);
                idx = end + 3;
            }

            // single node → return directly
            if (parts.size() == 1) return parts.get(0);

            // multiple → fragment
            Map<String, Object> fragment = new LinkedHashMap<>();
            fragment.put("type", "fragment");
            fragment.put("children", parts);
            return fragment;
        }


        // ---------- ELEMENT ----------
        if (!(node instanceof Element el)) return null;

        String tag = el.tagName().toLowerCase(Locale.ROOT);
        if (!ALLOWED_TAGS.contains(tag)) return null;

        // ---------- THYMELEAF BINDING (th:text / th:utext) ----------
        String thText = el.hasAttr("th:text") ? el.attr("th:text") : null;
        String thUtext = el.hasAttr("th:utext") ? el.attr("th:utext") : null;

        String expr = thText != null ? thText : thUtext;

        if (expr != null && expr.startsWith("${") && expr.endsWith("}")) {

            String key = expr.substring(2, expr.length() - 1).trim();

            // build fallback from original children
            List<Object> fallbackChildren = new ArrayList<>();
            for (Node c : el.childNodes()) {
                Object compiled = compileNode(c);
                if (compiled != null) fallbackChildren.add(compiled);
            }

            Map<String, Object> fallback = new LinkedHashMap<>();
            fallback.put("type", "fragment");
            fallback.put("children", fallbackChildren);

            Map<String, Object> binding = new LinkedHashMap<>();
            binding.put("type", "binding");
            binding.put("key", key);
            binding.put("fallback", fallback);

            Map<String, Object> out = new LinkedHashMap<>();
            out.put("type", "element");
            out.put("tag", tag);

            Map<String, String> attrs = new LinkedHashMap<>();
            if (el.hasAttr("class")) attrs.put("class", el.attr("class"));
            if (el.hasAttr("style")) attrs.put("style", el.attr("style"));
            if (!attrs.isEmpty()) out.put("attrs", attrs);

            out.put("children", List.of(binding));
            return out;
        }

        // ---------- NORMAL ELEMENT ----------
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("type", "element");
        out.put("tag", tag);

        Map<String, String> attrs = new LinkedHashMap<>();
        if (el.hasAttr("class")) attrs.put("class", el.attr("class"));
        if (el.hasAttr("style")) attrs.put("style", el.attr("style"));
        if (!attrs.isEmpty()) out.put("attrs", attrs);

        if ("br".equals(tag)) return out;

        List<Object> children = new ArrayList<>();
        for (Node c : el.childNodes()) {
            Object compiled = compileNode(c);
            if (compiled != null) children.add(compiled);
        }

        if (!children.isEmpty()) out.put("children", children);
        return out;
    }
}
