package com.gpse.basis.web;

import com.gpse.basis.domain.GleisLageRange;
import com.gpse.basis.services.RangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Range controller.
 */
@RestController
@RequestMapping("/api/range")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RangeController {
    private RangeService service;

    @Autowired
    RangeController(RangeService sr) {
        service = sr;
    }

    @GetMapping("/getGleisLage")
    public List<GleisLageRange> getGleisLageRange() {
        return service.getGleisLageRange();
    }

    @PostMapping("/postGleisLage")
    public void postGleisLage(@RequestBody List<GleisLageRange> lst) {
        service.saveGleisLageRange(lst);
    }

}
