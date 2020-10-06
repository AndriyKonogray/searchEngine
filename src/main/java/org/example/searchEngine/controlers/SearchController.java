package org.example.searchEngine.controlers;

import org.apache.lucene.document.Document;
import org.example.searchEngine.DTO.WebPageDTO;
import org.example.searchEngine.services.PageService.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.searchEngine.services.indexer.FieldNames.*;

@Controller
public class SearchController {

    @Autowired
    private PageService pageService;

    @GetMapping("/search")
    public String search(@RequestParam(name = "q") String q, Model model) throws IOException {
        List<Document> pages = pageService.searchPage(q);
        List<WebPageDTO> webPageDTOList = pages.stream()
                .map(document -> {
                    WebPageDTO webPageDTO = new WebPageDTO();
                    webPageDTO.setQ(document.get(URL.getName()));
                    webPageDTO.setTitle(document.get(TITLE.getName()));
                    return webPageDTO;
                })
                .collect(Collectors.toList());
        model.addAttribute("webPageDTOList", webPageDTOList);
        model.addAttribute("q", q);
        return "search";
    }
}
