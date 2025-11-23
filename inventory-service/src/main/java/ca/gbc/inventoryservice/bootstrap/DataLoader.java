package ca.gbc.inventoryservice.bootstrap;

import ca.gbc.inventoryservice.model.Inventory;
import ca.gbc.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;
    private final Environment environment;

    @Override
    public void run(String... args) throws Exception {
        // Skip loading data if we're running tests
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("test".equals(profile)) {
                return; // Exit early for tests
            }
        }

        if (inventoryRepository.count() == 0) {
            // Load initial data here
            Inventory inventory1 = Inventory.builder()
                    .skuCode("SKU001")
                    .quantity(100)
                    .build();

            Inventory inventory2 = Inventory.builder()
                    .skuCode("SKU002")
                    .quantity(50)
                    .build();

            inventoryRepository.save(inventory1);
            inventoryRepository.save(inventory2);
        }
    }
}