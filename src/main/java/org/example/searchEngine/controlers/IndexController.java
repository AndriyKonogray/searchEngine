package org.example.searchEngine.controlers;

import org.example.searchEngine.DTO.WebPageDTO;
import org.example.searchEngine.services.PageService.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class IndexController {

    @Autowired
    private PageService pageService;

    @PostMapping("/index")
    public String indexSubmit(@ModelAttribute WebPageDTO webPageDTO, Model model) throws IOException {
        pageService.indexPages(webPageDTO.getQ());
        model.addAttribute("q", webPageDTO);
        return "indexPagesStart";
    }

    @GetMapping("/index")
    public String indexForm(Model model) {
        model.addAttribute("page", new WebPageDTO());
        return "indexPages";
    }
}
