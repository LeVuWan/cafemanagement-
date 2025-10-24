package com.windy.cafemanagement.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.dto.ChooseMenuDto;
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

    public Menu getMenuByIdService(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại hoặc đã bị xóa"));
    }

    public List<MenuDetail> getListMenuDetailByMenu(Long id) {
        return menuDetailRepository.findByMenu_MenuIdAndIsDeletedFalse(id);
    }

    public void updateMenuService(MenuDTO menuDTO) {
        Menu updateMenu = menuRepository.findById(menuDTO.getMenuId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy menu với id: " + menuDTO.getMenuId()));

        updateMenu.setDishName(menuDTO.getName());
        updateMenu.setCurrentPrice(menuDTO.getPrice());

        List<MenuDetail> menuDetails = menuDetailRepository.findByMenu_MenuIdAndIsDeletedFalse(menuDTO.getMenuId());

        for (MenuDetail menuDetail : menuDetails) {
            menuDetail.setIsDeleted(true);
        }

        menuDetailRepository.saveAll(menuDetails);

        List<MenuDetail> newMenuDetails = new ArrayList<>();

        for (MenuIngredientDTO ing : menuDTO.getIngredients()) {
            Product product = productRepository.findById(ing.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + ing.getProductId()));

            Unit unit = unitRepository.findById(ing.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị ID: " + ing.getUnitId()));

            MenuDetail detail = new MenuDetail();
            detail.setMenu(updateMenu);
            detail.setProduct(product);
            detail.setUnit(unit);
            detail.setQuantity(ing.getQuantity());
            detail.setIsDeleted(false);
            newMenuDetails.add(detail);
        }

        menuDetailRepository.saveAll(newMenuDetails);

        menuRepository.save(updateMenu);
    }

    public void deleteMenuById(Long id) {
        System.out.println("Run here");
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy menu với id: " + id));
        List<MenuDetail> menuDetails = menuDetailRepository.findByMenu_MenuIdAndIsDeletedFalse(id);

        menu.setIsDeleted(true);

        for (MenuDetail menuDetail : menuDetails) {
            menuDetail.setIsDeleted(true);
        }

        menuDetailRepository.saveAll(menuDetails);

        menuRepository.save(menu);

    }

}
