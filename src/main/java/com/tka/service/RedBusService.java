package com.tka.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tka.entity.Bus;
import com.tka.entity.User;
import com.tka.repo.BusRepository;
import com.tka.repo.UserRepository;

@Service
public class RedBusService {

    @Autowired 
    private UserRepository userRepo;
    
    @Autowired 
    private BusRepository busRepo;

    // --- USER LOGIC ---
    public User login(String email, String password) {
        User u = userRepo.findByEmail(email);
        // Lombok ke @Data se u.getPassword() mil jayega
        if(u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    public String saveUser(User user) {
        if (userRepo.findByEmail(user.getEmail()) != null) {
            return "Email already exists!";
        }
        userRepo.save(user);
        return "User registered successfully";
    }

    // --- BUS SEARCH LOGIC ---
    public List<Bus> searchBuses(String from, String to) {
        // Ye method repository mein hona chahiye: 
        // List<Bus> findBySourceAndDestination(String source, String destination);
        return busRepo.findBySourceAndDestination(from, to);
    }

    // --- BOOKING LOGIC ---
    public boolean bookTicket(Long busId, int seatsToBook) {
        Bus bus = busRepo.findById(busId).orElse(null);
        
        // Lombok @Data use kar rahe ho toh field names check kar lena
        if (bus != null && bus.getAvailableSeats() >= seatsToBook) {
            bus.setAvailableSeats(bus.getAvailableSeats() - seatsToBook);
            busRepo.save(bus);
            return true;
        }
        return false;
    }

    // --- ADMIN/HELPER METHODS ---
    public List<Bus> getAllBuses() {
        return busRepo.findAll();
    }
    
    public void saveBus(Bus bus) { 
        busRepo.save(bus); 
    }

    public void deleteBus(Long id) {
        busRepo.deleteById(id);
    }
    
    public List<Bus> findBuses(String from, String to) { // naam change kiya
        return busRepo.findBySourceAndDestination(from, to);
    }
}