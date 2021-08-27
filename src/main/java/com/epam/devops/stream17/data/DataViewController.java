package com.epam.devops.stream17.data;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@Timed
@RequestMapping("/data")
public class DataViewController {
    @Autowired
    private DataService service;

    private final int ROW_PER_PAGE = 15;

    @Value("${backend.api.update}")
    private String BACKEND_UPDATE_API;
    private RestTemplateBuilder builder = new RestTemplateBuilder();
    private RestTemplate restTemplate = builder.build();

    @GetMapping(path="")
    public String show(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber, ArrayList<String> result) {
        List<CovidData> data = service.findAll(pageNumber, ROW_PER_PAGE);

        long count = service.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        if (!ObjectUtils.isEmpty(result)) {
            model.addAttribute("result", result.get(0));
            model.addAttribute("execTime", result.get(1));
        }
        model.addAttribute("data", data);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "index";
    }

    @GetMapping(path="/update")
    public String update(Model model) {
        ArrayList<String> result = new ArrayList<>();
        long start = System.currentTimeMillis();
        try {
            result.add(restTemplate.getForObject(BACKEND_UPDATE_API, String.class));
        } catch (Exception e) {
            result.add(e.getMessage());
        }
        long stop = System.currentTimeMillis();
        result.add(String.valueOf(stop - start));
        return show(model, 1, result);
    }

}
