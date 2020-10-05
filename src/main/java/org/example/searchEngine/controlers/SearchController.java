package org.example.searchEngine.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam(name="q") String q, Model model) {
        model.addAttribute("q", q);
        return "search";
    }
}
