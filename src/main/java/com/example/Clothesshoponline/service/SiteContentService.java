package com.example.Clothesshoponline.service;

import com.example.Clothesshoponline.model.SiteContent;
import com.example.Clothesshoponline.repository.SiteContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SiteContentService {

    @Autowired
    private SiteContentRepository repository;

    public String getContent(String key, String defaultValue) {
        return repository.findByContentKey(key)
                .map(SiteContent::getContentValue)
                .orElse(defaultValue);
    }

    public Optional<SiteContent> getContentByKey(String key) {
        return repository.findByContentKey(key);
    }

    public Map<String, String> getAllContentAsMap() {
        Map<String, String> contentMap = new HashMap<>();
        repository.findAll().forEach(c -> contentMap.put(c.getContentKey(), c.getContentValue()));
        return contentMap;
    }

    public List<SiteContent> getAllContent() {
        return repository.findAll();
    }

    public SiteContent saveContent(String key, String value, String type, String description, String updatedBy) {
        SiteContent content = repository.findByContentKey(key)
                .orElse(new SiteContent(key, value, type, description));

        content.setContentValue(value);
        content.setUpdatedAt(LocalDateTime.now());
        content.setUpdatedBy(updatedBy);

        return repository.save(content);
    }

    public SiteContent updateContent(String key, String value, String updatedBy) {
        Optional<SiteContent> optContent = repository.findByContentKey(key);
        if (optContent.isPresent()) {
            SiteContent content = optContent.get();
            content.setContentValue(value);
            content.setUpdatedAt(LocalDateTime.now());
            content.setUpdatedBy(updatedBy);
            return repository.save(content);
        } else {
            // Create new content if not exists
            SiteContent newContent = new SiteContent(key, value, "text", "Auto-created");
            newContent.setUpdatedBy(updatedBy);
            return repository.save(newContent);
        }
    }

    public void initializeDefaultContent() {
        // Initialize default content if not exists
        saveIfNotExists("hero_title", "LỄ HỘI MUA SẮM", "text", "Tiêu đề banner chính");
        saveIfNotExists("hero_subtitle", "CLOTHESSHOP SALE", "text", "Phụ đề banner chính");
        saveIfNotExists("hero_discount", "50", "text", "Phần trăm giảm giá hiển thị");
        saveIfNotExists("hero_deal_text", "MUA 2 NHẬN 3", "text", "Text deal đặc biệt");
        saveIfNotExists("promo_text", "CLOTHESSHOP SALE | GIẢM TỚI 50% | MUA 2 NHẬN 3 | HOT DEALS", "text",
                "Text thanh promo trên cùng");
        saveIfNotExists("shipping_text", "FREESHIP ĐƠN TỪ 499K", "text", "Text freeship");
        saveIfNotExists("contact_phone", "0123 456 789", "text", "Số điện thoại liên hệ");
        saveIfNotExists("contact_email", "admin@example.com", "text", "Email liên hệ");
        saveIfNotExists("contact_address", "ĐH Phenikaa / Thành phố Hà Nội", "text", "Địa chỉ");

        // Section titles
        saveIfNotExists("section_title_new_products", "SẢN PHẨM MỚI", "text", "Tiêu đề section Sản phẩm mới");
        saveIfNotExists("section_title_collections", "BỘ SƯU TẬP ĐẶC BIỆT", "text", "Tiêu đề section Bộ sưu tập");
        saveIfNotExists("section_title_kids", "THỜI TRANG TRẺ EM", "text", "Tiêu đề section Thời trang trẻ em");
        saveIfNotExists("section_title_blog", "TIN TỨC & CẢM HỨNG", "text", "Tiêu đề section Tin tức");

        // Category Banners - Nữ
        saveIfNotExists("banner_img_woman_jacket",
                "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800&h=600&fit=crop", "image",
                "Ảnh banner áo khoác nữ");
        saveIfNotExists("banner_title_woman_jacket", "ÁO KHOÁC NỮ", "text", "Tiêu đề banner áo khoác nữ");
        saveIfNotExists("banner_img_woman_sweater",
                "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=600&h=400&fit=crop", "image",
                "Ảnh banner áo len nữ");
        saveIfNotExists("banner_title_woman_sweater", "ÁO LEN NỮ", "text", "Tiêu đề banner áo len nữ");
        saveIfNotExists("banner_img_woman_homewear",
                "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&h=400&fit=crop", "image",
                "Ảnh banner đồ mặc nhà nữ");
        saveIfNotExists("banner_title_woman_homewear", "ĐỒ MẶC NHÀ", "text", "Tiêu đề banner đồ mặc nhà nữ");

        // Category Banners - Nam
        saveIfNotExists("banner_img_man_jacket",
                "https://images.unsplash.com/photo-1617137968427-85924c800a22?w=600&h=400&fit=crop", "image",
                "Ảnh banner áo khoác nam");
        saveIfNotExists("banner_title_man_jacket", "ÁO KHOÁC NAM", "text", "Tiêu đề banner áo khoác nam");
        saveIfNotExists("banner_img_man_sweater",
                "https://images.unsplash.com/photo-1516257984-b1b4d707412e?w=600&h=400&fit=crop", "image",
                "Ảnh banner áo len nam");
        saveIfNotExists("banner_title_man_sweater", "ÁO LEN NAM", "text", "Tiêu đề banner áo len nam");
        saveIfNotExists("banner_img_man_homewear",
                "https://images.unsplash.com/photo-1490578474895-699cd4e2cf59?w=800&h=600&fit=crop", "image",
                "Ảnh banner đồ mặc nhà nam");
        saveIfNotExists("banner_title_man_homewear", "ĐỒ MẶC NHÀ NAM", "text", "Tiêu đề banner đồ mặc nhà nam");

        // Collections
        saveIfNotExists("collection_img_disney",
                "https://images.unsplash.com/photo-1518831959646-742c3a14ebf7?w=500&h=400&fit=crop", "image",
                "Ảnh BST Disney");
        saveIfNotExists("collection_name_disney", "Disney Collection", "text", "Tên BST Disney");
        saveIfNotExists("collection_img_doraemon",
                "https://images.unsplash.com/photo-1503919545889-aef636e10ad4?w=500&h=400&fit=crop", "image",
                "Ảnh BST Doraemon");
        saveIfNotExists("collection_name_doraemon", "Doraemon Collection", "text", "Tên BST Doraemon");
        saveIfNotExists("collection_img_canifa",
                "https://images.unsplash.com/photo-1445205170230-053b83016050?w=500&h=400&fit=crop", "image",
                "Ảnh BST Canifa S");
        saveIfNotExists("collection_name_canifa", "Canifa S", "text", "Tên BST Canifa S");

        // Kids Section
        saveIfNotExists("kids_img_girl",
                "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?w=600&h=500&fit=crop", "image",
                "Ảnh banner bé gái");
        saveIfNotExists("kids_title_girl", "BÉ GÁI", "text", "Tiêu đề banner bé gái");
        saveIfNotExists("kids_img_boy",
                "https://images.unsplash.com/photo-1519457431-44ccd64a579b?w=600&h=500&fit=crop", "image",
                "Ảnh banner bé trai");
        saveIfNotExists("kids_title_boy", "BÉ TRAI", "text", "Tiêu đề banner bé trai");

        // Banner Links - Nữ
        saveIfNotExists("banner_link_woman_jacket", "/products?category=NỮ", "link", "Link banner áo khoác nữ");
        saveIfNotExists("banner_link_woman_sweater", "/products?category=NỮ", "link", "Link banner áo len nữ");
        saveIfNotExists("banner_link_woman_homewear", "/products?category=NỮ", "link", "Link banner đồ mặc nhà nữ");

        // Banner Links - Nam
        saveIfNotExists("banner_link_man_jacket", "/products?category=NAM", "link", "Link banner áo khoác nam");
        saveIfNotExists("banner_link_man_sweater", "/products?category=NAM", "link", "Link banner áo len nam");
        saveIfNotExists("banner_link_man_homewear", "/products?category=NAM", "link", "Link banner đồ mặc nhà nam");

        // Collection Links
        saveIfNotExists("collection_link_disney", "/products?category=BÉ GÁI", "link", "Link BST Disney");
        saveIfNotExists("collection_link_doraemon", "/products?category=BÉ TRAI", "link", "Link BST Doraemon");
        saveIfNotExists("collection_link_canifa", "/products", "link", "Link BST Canifa S");

        // Kids Links
        saveIfNotExists("kids_link_girl", "/products?category=BÉ GÁI", "link", "Link banner bé gái");
        saveIfNotExists("kids_link_boy", "/products?category=BÉ TRAI", "link", "Link banner bé trai");
    }

    private void saveIfNotExists(String key, String value, String type, String description) {
        if (repository.findByContentKey(key).isEmpty()) {
            repository.save(new SiteContent(key, value, type, description));
        }
    }
}
