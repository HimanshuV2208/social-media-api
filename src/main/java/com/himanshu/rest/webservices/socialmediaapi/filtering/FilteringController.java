package com.himanshu.rest.webservices.socialmediaapi.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public MappingJacksonValue filtering() {
        SomeBean someBean = new SomeBean("val1", "val2", "val3");

        //return only field1 and field3
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
        FilterProvider filers = getFilterProvider(new String[]{"field1", "field3"});
        mappingJacksonValue.setFilters(filers);

        return mappingJacksonValue;
    }

    @GetMapping("/filtering-list")
    public MappingJacksonValue filteringList() {
        List<SomeBean> list = Arrays.asList(new SomeBean("val1", "val2", "val3"),
                new SomeBean("val4", "val5", "val6"));
        //return only field2 and field3
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        FilterProvider filters = getFilterProvider(new String[]{"field2", "field3"});
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    private FilterProvider getFilterProvider(String[] fields) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
        FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
        return filters;
    }

}
