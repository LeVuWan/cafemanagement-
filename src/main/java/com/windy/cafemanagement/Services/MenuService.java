package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.dto.MenuDTO;
import com.windy.cafemanagement.dto.MenuIngredientDTO;
import com.windy.cafemanagement.models.Menu;
import com.windy.cafemanagement.models.MenuDetail;
import com.windy.cafemanagement.models.Product;
import com.windy.cafemanagement.models.Unit;
import com.windy.cafemanagement.repositories.MenuDetailRepository;
import com.windy.cafemanagement.repositories.MenuRepository;
import com.windy.cafemanagement.repositories.ProductRepository;
import com.windy.cafemanagement.repositories.UnitRepository;

import jakarta.transaction.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuDetailRepository menuDetailRepository;
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;

    public MenuService(MenuRepository menuRepository, MenuDetailRepository menuDetailRepository,
            ProductRepository productRepository, UnitRepository unitRepository) {
        this.menuRepository = menuRepository;
        this.menuDetailRepository = menuDetailRepository;
        this.productRepository = productRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional
    public void createMenuService(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setDishName(menuDTO.getName());
        menu.setCurrentPrice(menuDTO.getPrice());
        menu.setIsDeleted(false);
        Menu savedMenu = menuRepository.save(menu);

        if (menuDTO.getIngredients() != null && !menuDTO.getIngredients().isEmpty()) {
            for (MenuIngredientDTO ing : menuDTO.getIngredients()) {

                Product product = productRepository.findById(ing.getProductId())
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại hoặc đã bị xóa"));

                Unit unit = unitRepository.findById(ing.getUnitId())
                        .orElseThrow(() -> new RuntimeException("Đơn vị tính không tồn tại hoặc đã bị xóa"));

                MenuDetail detail = new MenuDetail();
                detail.setMenu(savedMenu);
                detail.setProduct(product);
                detail.setUnit(unit);
                detail.setQuantity(ing.getQuantity());
                detail.setIsDeleted(false);

                menuDetailRepository.save(detail);
            }
        }
    }

    public List<Menu> getAllMenuService(String keyword) {
        if (keyword == null) {
            System.out.println("Run here");
             keyword = "";
        }
        return menuRepository.findAllMenu(keyword.trim());
    }
}
