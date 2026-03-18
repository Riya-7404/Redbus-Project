package com.tka.controller;


import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tka.entity.Booking;
import com.tka.entity.Bus;
import com.tka.entity.User;
import com.tka.repo.BookingRepository;
import com.tka.service.RedBusService;

@Controller
public class MainController {

    @Autowired
    private RedBusService service;
    
    
    @Autowired
    private BookingRepository bookingRepo;

    @GetMapping("/")
    public String showLandingPage() { 
        return "index"; 
    }

    @GetMapping("/login")
    public String showLoginPage() { 
        return "login"; 
    }

    @GetMapping("/signup")
    public String showSignupPage() { 
        return "signup"; 
    }

    @GetMapping("/payment")
    public String showPayment() { 
        return "payment"; 
    }
    
    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute User user, 
                               @RequestParam("firstName") String fname, 
                               @RequestParam("lastName") String lname) {
        user.setName(fname + " " + lname);
        user.setRole("CUSTOMER");
        String result = service.saveUser(user);
        
        if (result.equals("User registered successfully")) {
            return "redirect:/login?success=true"; 
        } else {
            return "redirect:/signup?error=" + result;
        }
    }

    @PostMapping("/loginUser")
    public String loginUser(@RequestParam("email") String email, 
                            @RequestParam("password") String password,
                            HttpSession session) {
        User user = service.login(email, password);
        
        if (user != null) {
            session.setAttribute("user", user);
            // SUCCESS: Yahan redirect ho raha hai
            return "redirect:/index?login=success"; 
        } else {
            return "redirect:/login?error=invalid";
        }
    }

    @GetMapping("/index")
    public String showIndex() {
        return "index"; 
    }
    
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // 1. Saari buses fetch karein (taaki screen khaali na dikhe)
        List<Bus> allBuses = service.getAllBuses(); 
        
        // 2. Booking history fetch karein (taaki "My Recent Bookings" dikhe)
        List<Booking> history = bookingRepo.findAllByOrderByIdDesc(); 

        // 3. Model mein data add karein
        model.addAttribute("buses", allBuses);
        model.addAttribute("myBookings", history);
        
        // Heading fix karne ke liye
        model.addAttribute("fromCity", "All Routes");
        model.addAttribute("toCity", "Available");

        return "dashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
    
   
    
    @GetMapping("/help")
    public String showHelpPage() {
        return "help";
    }

    @GetMapping("/offers")
    public String showOffersPage() {
        return "offers";
    }

    @GetMapping("/ai-tech")
    public String showAiPage() {
        return "ai-tech";
    }
    
    @PostMapping("/searchBuses")
    public String handleSearch(@RequestParam("from") String from, 
                               @RequestParam("to") String to, 
                               @RequestParam("travelDate") String date,
                               Model model) {
        
    	List<Bus> buses = service.searchBuses(from, to);
        
        model.addAttribute("buses", buses);
        model.addAttribute("fromCity", from);
        model.addAttribute("toCity", to);
        
        return "dashboard"; // Ab dashboard.html par buses ki list dikhegi
    }
    
    @PostMapping("/confirmBooking")
    public String confirmBooking(@RequestParam("selectedSeats") String seats, 
                                 @RequestParam("totalAmount") double amount, 
                                 Model model) {
        model.addAttribute("seats", seats);
        model.addAttribute("amount", amount);
        return "payment"; // Ye aapka Payment page open karega
    }

    @GetMapping("/paymentSuccess")
    public String paymentSuccess(@RequestParam String seats, @RequestParam double amount, Model model) {
        // Booking save karein (Real App mein aap source/dest bhi pass karein)
        Booking b = new Booking();
        b.setSource("Mumbai");
        b.setDestination("Pune");
        b.setSeatNumbers(seats);
        b.setAmount(amount);
        b.setBookingDate("01 March 2026");
        bookingRepo.save(b);

        model.addAttribute("seats", seats);
        model.addAttribute("amount", amount);
        return "success"; 
    }
    
    @PostMapping("/cancelBooking")
    public String cancelBooking(@RequestParam("bookingId") Long id, RedirectAttributes ra) {
        try {
            bookingRepo.deleteById(id);
            ra.addFlashAttribute("message", "Ticket cancelled successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error cancelling ticket.");
        }
        return "redirect:/dashboard"; // Refresh hoke wapas dashboard par jayega
    }
    
    @GetMapping("/bus-tickets")
    public String viewAllBusTickets(Model model) {
        // ERROR FIX: 'RedBusService' (Capital R) ko hata kar 'service' (Small s) kiya gaya hai
        // Kyunki aapne line 24 par variable ka naam 'service' rakha hai
        List<Bus> allBuses = service.getAllBuses(); 
        
        allBuses.forEach(bus -> {
            // Safe check: Agar distance null hai toh 0 maanein
            Integer distVal = bus.getDistanceKm();
            int dist = (distVal != null) ? distVal : 0;
            
            // Safe check: Agar busType null hai toh empty string maanein
            String type = (bus.getBusType() != null) ? bus.getBusType() : "";
            
            // Logic: Sleeper ke liye ₹3/km, Seater ke liye ₹2/km
            double baseRate = (type.contains("Sleeper")) ? 3.0 : 2.0;
            double calculatedPrice = dist * baseRate;
            
            // AC hai toh ₹250 extra
            if(type.contains("AC")) {
                calculatedPrice += 250.0;
            }
            
            bus.setPrice(calculatedPrice);
        });

        model.addAttribute("allBuses", allBuses);
        return "all-buses"; 
    }
    
    
        
}