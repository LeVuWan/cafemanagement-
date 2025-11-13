package com.windy.cafemanagement.Services;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.windy.cafemanagement.dto.MenuDTO;
import com.windy.cafemanagement.dto.MenuIngredientDTO;
import com.windy.cafemanagement.models.Menu;
import com.windy.cafemanagement.models.MenuDetail;
import com.windy.cafemanagement.models.MenuDetailId;
import com.windy.cafemanagement.models.Product;
import com.windy.cafemanagement.models.Unit;
import com.windy.cafemanagement.repositories.MenuDetailRepository;
import com.windy.cafemanagement.repositories.MenuRepository;
import com.windy.cafemanagement.repositories.ProductRepository;
import com.windy.cafemanagement.repositories.UnitRepository;

/**
 * MenuService
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025 VuLQ Create
 */
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

    /**
     * Create menu with menu details
     * 
     * @param menuDTO
     * @throws EntityNotFoundException, DataAccessException
     */
    @Transactional
    public void createMenuService(MenuDTO menuDTO) {
        if (menuDTO == null) {
            throw new NullPointerException("menuDTO not found");
        }

        Menu menu = new Menu();
        menu.setDishName(menuDTO.getName());
        menu.setCurrentPrice(menuDTO.getPrice());
        menu.setIsDeleted(false);
        Menu savedMenu = menuRepository.save(menu);

        if (menuDTO.getIngredients() != null && !menuDTO.getIngredients().isEmpty()) {
            for (MenuIngredientDTO ing : menuDTO.getIngredients()) {

                Product product = productRepository.findById(ing.getProductId())
                        .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));

                Unit unit = unitRepository.findById(ing.getUnitId())
                        .orElseThrow(() -> new EntityNotFoundException("Đơn vị tính không tồn tại hoặc đã bị xóa"));

                MenuDetail detail = new MenuDetail();
                MenuDetailId id = new MenuDetailId(menu.getMenuId(), ing.getProductId());

                detail.setMenuDetailId(id);
                detail.setMenu(savedMenu);
                detail.setProduct(product);
                detail.setUnit(unit);
                detail.setQuantity(ing.getQuantity());
                detail.setIsDeleted(false);
                menuDetailRepository.save(detail);
            }
        }
    }

    /**
     * get all menu by keyword
     * 
     * @param keyword
     * @return List<Menu>
     * @throws DataAccessException
     */
    public List<Menu> getAllMenuService(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        return menuRepository.findAllMenu(keyword.trim());
    }

    /**
     * get menu by id
     * 
     * @param id
     * @return Menu
     * @throws DataAccessException
     */
    public Menu getMenuByIdService(Long id) throws DataAccessException {
        return menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));
    }

    /**
     * get list menu detail by menu id
     * 
     * @param id
     * @return List<MenuDetail>
     * @throws DataAccessException
     */
    public List<MenuDetail> getListMenuDetailByMenu(Long id) throws DataAccessException {
        List<MenuDetail> menuDetails = menuRepository.findMenuDetailByMenu(id);

        return menuDetails;
    }

    /**
     * update menu and menu details
     * 
     * @param menuDTO
     * @throws DataAccessException, EntityNotFoundException
     */
    public void updateMenuService(MenuDTO menuDTO) {
        System.out.println("Check menuDTO: " + menuDTO.getIngredients().size());
        Menu updateMenu = menuRepository.findById(menuDTO.getMenuId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy menu với id: " + menuDTO.getMenuId()));

        updateMenu.setDishName(menuDTO.getName());
        updateMenu.setCurrentPrice(menuDTO.getPrice());

        List<MenuDetail> menuDetails = getListMenuDetailByMenu(menuDTO.getMenuId());

        for (MenuDetail menuDetail : menuDetails) {
            menuDetail.setIsDeleted(true);
        }

        menuDetailRepository.saveAll(menuDetails);

        List<MenuDetail> newMenuDetails = new ArrayList<>();

        for (MenuIngredientDTO ing : menuDTO.getIngredients()) {
            Product product = productRepository.findById(ing.getProductId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Không tìm thấy sản phẩm ID: " + ing.getProductId()));

            Unit unit = unitRepository.findById(ing.getUnitId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn vị ID: " + ing.getUnitId()));

            MenuDetail detail = new MenuDetail();

            MenuDetailId id = new MenuDetailId(updateMenu.getMenuId(), ing.getProductId());
            detail.setMenuDetailId(id);
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

    /**
     * delete menu by id
     * 
     * @param id
     * @throws DataAccessException, EntityNotFoundException
     */
    public void deleteMenuById(Long id) throws DataAccessException, EntityNotFoundException {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy menu với id: " + id));
        List<MenuDetail> menuDetails = getListMenuDetailByMenu(id);

        menu.setIsDeleted(true);

        for (MenuDetail menuDetail : menuDetails) {
            menuDetail.setIsDeleted(true);
        }

        menuDetailRepository.saveAll(menuDetails);

        menuRepository.save(menu);

    }

}
