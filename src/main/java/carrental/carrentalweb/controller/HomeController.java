package carrental.carrentalweb.controller;

import carrental.carrentalweb.entities.User;
import carrental.carrentalweb.repository.CarRepository;
import carrental.carrentalweb.repository.CreditRatingRepository;
import carrental.carrentalweb.repository.PickupPointRepository;
import carrental.carrentalweb.repository.SubscriptionRepository;
import carrental.carrentalweb.repository.UserRepository;
import carrental.carrentalweb.services.TimeOfDayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Written by Nicolai Berg Andersen
 */
@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PickupPointRepository pickupPointRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    CreditRatingRepository creditRatingRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    TimeOfDayService timeOfDayService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal User user) {
        // Used by car booking widget.
        model.addAttribute("user", user);
        model.addAttribute("creditRating", user == null ? null : creditRatingRepository.find("user_id", user.getId()));
        model.addAttribute("cars", carRepository.last(3));
        model.addAttribute("pickupPoints", pickupPointRepository.last(3));
        model.addAttribute("subscriptions", subscriptionRepository.last(3));
        model.addAttribute("timeOfDayImage", timeOfDayService.getImage());
        return "home/index";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pickupPoints", pickupPointRepository.last(3));
        return "home/contact";
    }

    @GetMapping("/agreement")
    public String css_framework() {
        return "home/agreement";
    }
}
