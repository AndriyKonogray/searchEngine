package org.example.searchEngine.controlers;

import org.example.searchEngine.DTO.WebPageDTO;
import org.example.searchEngine.model.WebPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {

    @PostMapping("/index")
    public String indexSubmit(@ModelAttribute WebPageDTO webPageDTO, Model model) {
        WebPage page = WebPage.newBuilder()
                .setURL(webPageDTO.getQ())
                .build();
        model.addAttribute("q", webPageDTO);
        return "indexPagesStart";
    }
    @GetMapping("/index")
    public String indexForm(Model model) {
        model.addAttribute("page", new WebPageDTO());
        return "indexPages";
    }
}
