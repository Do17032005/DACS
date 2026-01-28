/* Admin Inline Editor - Cho phép admin chỉnh sửa nội dung trực tiếp trên website */

(function() {
    'use strict';

    // Check if user is admin
    const isAdmin = document.body.classList.contains('is-admin');
    if (!isAdmin) return;

    // Create edit toolbar
    const toolbar = document.createElement('div');
    toolbar.id = 'admin-toolbar';
    toolbar.innerHTML = `
        <div class="admin-toolbar-content">
            <span class="admin-badge">🛠️ Chế độ Admin</span>
            <button id="toggle-edit-mode" class="admin-btn">✏️ Bật chỉnh sửa</button>
            <button id="auto-detect-btn" class="admin-btn" style="display:none;">🔍 Tự động phát hiện</button>
            <button id="save-all-changes" class="admin-btn save-btn" style="display:none;">💾 Lưu tất cả</button>
            <button id="cancel-changes" class="admin-btn cancel-btn" style="display:none;">❌ Hủy</button>
            <span id="edit-status" class="edit-status"></span>
        </div>
    `;
    document.body.appendChild(toolbar);

    // Add toolbar styles
    const style = document.createElement('style');
    style.textContent = `
        #admin-toolbar {
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
            color: white;
            padding: 12px 20px;
            z-index: 99999;
            box-shadow: 0 -4px 20px rgba(0,0,0,0.3);
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }
        .admin-toolbar-content {
            max-width: 1400px;
            margin: 0 auto;
            display: flex;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
        }
        .admin-badge {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            padding: 6px 14px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 13px;
        }
        .admin-btn {
            background: rgba(255,255,255,0.1);
            border: 1px solid rgba(255,255,255,0.2);
            color: white;
            padding: 8px 16px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 13px;
            transition: all 0.2s;
        }
        .admin-btn:hover {
            background: rgba(255,255,255,0.2);
            transform: translateY(-1px);
        }
        .admin-btn.active {
            background: #4CAF50;
            border-color: #4CAF50;
        }
        .admin-btn.save-btn {
            background: #4CAF50;
            border-color: #4CAF50;
        }
        .admin-btn.cancel-btn {
            background: #f44336;
            border-color: #f44336;
        }
        .edit-status {
            margin-left: auto;
            font-size: 13px;
            opacity: 0.8;
        }
        
        /* Editable elements styling */
        .admin-editable {
            outline: 2px dashed #4CAF50 !important;
            outline-offset: 3px;
            cursor: text;
            min-height: 20px;
            position: relative;
        }
        .admin-editable:hover {
            outline-color: #2196F3;
            background: rgba(33, 150, 243, 0.1) !important;
        }
        .admin-editable:focus {
            outline: 2px solid #2196F3 !important;
            background: rgba(33, 150, 243, 0.15) !important;
        }
        .admin-editable.changed {
            outline-color: #FF9800 !important;
        }
        .admin-editable.saved {
            animation: savedPulse 0.5s ease;
        }
        @keyframes savedPulse {
            0%, 100% { outline-color: #4CAF50; }
            50% { outline-color: #8BC34A; box-shadow: 0 0 10px rgba(76, 175, 80, 0.5); }
        }

        /* Image edit overlay */
        .admin-editable-image {
            position: relative !important;
            cursor: pointer !important;
            outline: 2px dashed #9C27B0 !important;
            outline-offset: 3px;
        }
        .admin-editable-image:hover {
            outline-color: #E91E63 !important;
        }
        .admin-image-overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0,0,0,0.6);
            display: flex;
            align-items: center;
            justify-content: center;
            opacity: 0;
            transition: opacity 0.2s;
            pointer-events: none;
            z-index: 100;
        }
        .admin-editable-image:hover .admin-image-overlay {
            opacity: 1;
        }
        .admin-image-overlay span {
            background: white;
            color: #333;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 14px;
        }

        /* Add padding to body for toolbar */
        body.edit-mode-active {
            padding-bottom: 70px !important;
        }

        /* Edit modal */
        .admin-modal {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0,0,0,0.8);
            z-index: 100000;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .admin-modal-content {
            background: white;
            padding: 30px;
            border-radius: 12px;
            max-width: 600px;
            width: 90%;
            max-height: 80vh;
            overflow-y: auto;
        }
        .admin-modal h3 {
            margin: 0 0 20px 0;
            color: #333;
            font-size: 18px;
        }
        .admin-modal input, .admin-modal textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-bottom: 15px;
            font-size: 14px;
            box-sizing: border-box;
        }
        .admin-modal textarea {
            min-height: 100px;
            resize: vertical;
        }
        .admin-modal-preview {
            margin-bottom: 15px;
            text-align: center;
        }
        .admin-modal-preview img {
            max-width: 100%;
            max-height: 200px;
            border-radius: 8px;
        }
        .admin-modal-buttons {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
        }
        .admin-modal-buttons button {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
        }
        .admin-modal-buttons .btn-save {
            background: #4CAF50;
            color: white;
        }
        .admin-modal-buttons .btn-cancel {
            background: #ddd;
            color: #333;
        }
        .admin-modal-buttons .btn-upload {
            background: #2196F3;
            color: white;
        }

        /* Edit label tooltip */
        .admin-edit-label {
            position: absolute;
            top: -25px;
            left: 0;
            background: #2196F3;
            color: white;
            font-size: 11px;
            padding: 3px 8px;
            border-radius: 4px;
            white-space: nowrap;
            z-index: 1000;
            pointer-events: none;
            opacity: 0;
            transition: opacity 0.2s;
        }
        .admin-editable:hover .admin-edit-label,
        .admin-editable-image:hover .admin-edit-label {
            opacity: 1;
        }

        /* Link edit styling */
        .admin-editable-link {
            outline: 2px dashed #FF9800 !important;
            outline-offset: 3px;
        }
        .admin-editable-link:hover {
            outline-color: #E91E63;
        }
        .admin-editable-link.changed {
            outline-color: #4CAF50 !important;
        }
        .admin-link-edit-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%);
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 20px;
            cursor: pointer;
            font-size: 13px;
            font-weight: 600;
            z-index: 100;
            box-shadow: 0 2px 10px rgba(0,0,0,0.3);
            transition: all 0.2s;
        }
        .admin-link-edit-btn:hover {
            transform: scale(1.05);
            box-shadow: 0 4px 15px rgba(0,0,0,0.4);
        }
    `;
    document.head.appendChild(style);

    // State
    let editMode = false;
    let changedElements = new Map();
    let originalValues = new Map();
    let savedContent = new Map(); // Store content loaded from database

    // Text elements that can be edited
    const textSelectors = 'h1, h2, h3, h4, h5, h6, p, span, a, button, label, li, td, th, .hero-title, .hero-subtitle, .hero-deal-text, .section-title, .product-name, .product-price, .promo-text';
    
    // Image elements that can be edited
    const imageSelectors = 'img';

    // Elements to exclude from editing
    const excludeSelectors = '#admin-toolbar, #admin-toolbar *, .admin-modal, .admin-modal *, script, style, meta, link, input, select, textarea, [contenteditable], .swiper-button-next, .swiper-button-prev, .swiper-pagination, nav a, .nav-link, .header-icon-link, .account-dropdown-menu *, .mega-menu *, .footer-col a, .social-link';

    // Generate stable key based on element path in DOM (not content-based)
    function generateKey(el) {
        // First check if element has data-editable attribute (predefined key)
        if (el.dataset.editable) {
            return el.dataset.editable;
        }
        
        // Generate stable key based on DOM path instead of content
        const tag = el.tagName.toLowerCase();
        const id = el.id ? `#${el.id}` : '';
        const classes = Array.from(el.classList).filter(c => !c.startsWith('admin-')).sort().join('.');
        
        // Get parent section info for better context
        const section = el.closest('section');
        const sectionClass = section ? Array.from(section.classList).filter(c => !c.startsWith('admin-'))[0] || '' : '';
        
        // Get nth-of-type position among siblings with same tag
        let siblings = el.parentElement ? Array.from(el.parentElement.children).filter(s => s.tagName === el.tagName) : [];
        let position = siblings.indexOf(el) + 1;
        
        return `${sectionClass}_${tag}${id}${classes ? '.' + classes : ''}_${position}`;
    }

    // Load saved content from database and apply to page
    async function loadSavedContent() {
        try {
            const response = await fetch('/api/content');
            if (!response.ok) return;
            
            const contentMap = await response.json();
            
            // Store in savedContent map
            Object.entries(contentMap).forEach(([key, value]) => {
                savedContent.set(key, value);
            });
            
            // Apply to elements with data-editable attribute
            document.querySelectorAll('[data-editable]').forEach(el => {
                const key = el.dataset.editable;
                if (savedContent.has(key)) {
                    const value = savedContent.get(key);
                    if (el.tagName === 'IMG') {
                        el.src = value;
                    } else {
                        el.innerHTML = value;
                    }
                }
            });
            
            // Apply to elements with data-editable-link attribute (links)
            document.querySelectorAll('[data-editable-link]').forEach(el => {
                const key = el.dataset.editableLink;
                if (savedContent.has(key)) {
                    el.href = savedContent.get(key);
                }
            });
            
            // Apply to section titles and other elements based on generated keys
            const allEditableElements = [...document.querySelectorAll(textSelectors), ...document.querySelectorAll(imageSelectors)];
            allEditableElements.forEach(el => {
                if (isExcluded(el) || !isVisible(el)) return;
                if (el.dataset.editable) return; // Already handled above
                
                const key = generateKey(el);
                if (savedContent.has(key)) {
                    const value = savedContent.get(key);
                    if (el.tagName === 'IMG') {
                        el.src = value;
                    } else {
                        el.innerHTML = value;
                    }
                }
            });
            
            console.log('📦 Loaded saved content:', savedContent.size, 'items');
        } catch (error) {
            console.error('Error loading saved content:', error);
        }
    }

    function isExcluded(el) {
        if (!el || !el.matches) return true;
        try {
            return el.matches(excludeSelectors) || el.closest(excludeSelectors);
        } catch (e) {
            return true;
        }
    }

    function isVisible(el) {
        return el.offsetWidth > 0 && el.offsetHeight > 0;
    }

    // Toggle edit mode
    function toggleEditMode() {
        editMode = !editMode;
        const toggleBtn = document.getElementById('toggle-edit-mode');
        const autoBtn = document.getElementById('auto-detect-btn');
        const saveBtn = document.getElementById('save-all-changes');
        const cancelBtn = document.getElementById('cancel-changes');
        const status = document.getElementById('edit-status');

        if (editMode) {
            enableEditMode();
            document.body.classList.add('edit-mode-active');
            toggleBtn.textContent = '✏️ Đang chỉnh sửa...';
            toggleBtn.classList.add('active');
            autoBtn.style.display = 'inline-block';
            saveBtn.style.display = 'inline-block';
            cancelBtn.style.display = 'inline-block';
            status.textContent = 'Click vào nội dung để chỉnh sửa';
        } else {
            disableEditMode();
        }
    }

    function enableEditMode() {
        // Make text elements editable
        document.querySelectorAll(textSelectors).forEach(el => {
            if (isExcluded(el) || !isVisible(el)) return;
            if (el.children.length > 0 && el.children[0].tagName !== 'SPAN') return; // Skip containers
            
            const key = generateKey(el);
            el.classList.add('admin-editable');
            el.setAttribute('contenteditable', 'true');
            el.dataset.adminKey = key;
            
            // Store original value
            originalValues.set(key, el.innerHTML);
            
            // Add label
            const label = document.createElement('div');
            label.className = 'admin-edit-label';
            label.textContent = el.tagName.toLowerCase();
            el.style.position = 'relative';
            el.appendChild(label);
            
            // Listen for changes
            el.addEventListener('input', handleTextInput);
            el.addEventListener('blur', handleTextBlur);
        });

        // Make images editable
        document.querySelectorAll(imageSelectors).forEach(el => {
            if (isExcluded(el) || !isVisible(el)) return;
            
            const key = generateKey(el);
            el.classList.add('admin-editable-image');
            el.dataset.adminKey = key;
            el.style.position = 'relative';
            
            // Store original value
            originalValues.set(key, el.src);
            
            // Add overlay
            const overlay = document.createElement('div');
            overlay.className = 'admin-image-overlay';
            overlay.innerHTML = '<span>📷 Click để đổi ảnh</span>';
            
            // Wrap image if needed
            if (el.parentElement.style.position !== 'relative' && el.parentElement.style.position !== 'absolute') {
                el.parentElement.style.position = 'relative';
            }
            el.parentElement.insertBefore(overlay, el.nextSibling);
            overlay.style.width = el.offsetWidth + 'px';
            overlay.style.height = el.offsetHeight + 'px';
            overlay.style.position = 'absolute';
            overlay.style.top = el.offsetTop + 'px';
            overlay.style.left = el.offsetLeft + 'px';
            
            el.addEventListener('click', handleImageClick);
        });

        // Make links with data-editable-link editable
        document.querySelectorAll('[data-editable-link]').forEach(el => {
            if (isExcluded(el) || !isVisible(el)) return;
            
            const key = el.dataset.editableLink;
            el.classList.add('admin-editable-link');
            el.dataset.adminLinkKey = key;
            
            // Store original value
            originalValues.set(key, el.href);
            
            // Add link edit button
            const linkBtn = document.createElement('button');
            linkBtn.className = 'admin-link-edit-btn';
            linkBtn.innerHTML = '🔗 Sửa link';
            linkBtn.onclick = (e) => {
                e.preventDefault();
                e.stopPropagation();
                handleLinkClick(el, key);
            };
            
            if (el.style.position !== 'relative' && el.style.position !== 'absolute') {
                el.style.position = 'relative';
            }
            el.appendChild(linkBtn);
        });
    }

    function disableEditMode() {
        editMode = false;
        document.body.classList.remove('edit-mode-active');
        const toggleBtn = document.getElementById('toggle-edit-mode');
        const autoBtn = document.getElementById('auto-detect-btn');
        const saveBtn = document.getElementById('save-all-changes');
        const cancelBtn = document.getElementById('cancel-changes');
        const status = document.getElementById('edit-status');

        toggleBtn.textContent = '✏️ Bật chỉnh sửa';
        toggleBtn.classList.remove('active');
        autoBtn.style.display = 'none';
        saveBtn.style.display = 'none';
        cancelBtn.style.display = 'none';
        status.textContent = '';

        // Remove editable from text elements
        document.querySelectorAll('.admin-editable').forEach(el => {
            el.classList.remove('admin-editable', 'changed');
            el.removeAttribute('contenteditable');
            el.removeEventListener('input', handleTextInput);
            el.removeEventListener('blur', handleTextBlur);
            const label = el.querySelector('.admin-edit-label');
            if (label) label.remove();
        });

        // Remove editable from images
        document.querySelectorAll('.admin-editable-image').forEach(el => {
            el.classList.remove('admin-editable-image');
            el.removeEventListener('click', handleImageClick);
        });

        // Remove editable from links
        document.querySelectorAll('.admin-editable-link').forEach(el => {
            el.classList.remove('admin-editable-link', 'changed');
            const linkBtn = el.querySelector('.admin-link-edit-btn');
            if (linkBtn) linkBtn.remove();
        });

        // Remove overlays
        document.querySelectorAll('.admin-image-overlay').forEach(el => el.remove());
    }

    function handleTextInput(e) {
        const el = e.target;
        const key = el.dataset.adminKey;
        const newValue = el.innerHTML;
        const originalValue = originalValues.get(key);

        if (newValue !== originalValue) {
            el.classList.add('changed');
            changedElements.set(key, { type: 'text', element: el, value: newValue });
        } else {
            el.classList.remove('changed');
            changedElements.delete(key);
        }

        updateStatus();
    }

    function handleTextBlur(e) {
        // Optional: validation
    }

    function handleImageClick(e) {
        e.preventDefault();
        e.stopPropagation();
        
        const el = e.target;
        const key = el.dataset.adminKey;
        const currentSrc = el.src;

        showImageModal(key, currentSrc, el);
    }

    function handleLinkClick(el, key) {
        const currentHref = el.href;
        showLinkModal(key, currentHref, el);
    }

    function showLinkModal(key, currentHref, linkElement) {
        const modal = document.createElement('div');
        modal.className = 'admin-modal';
        modal.innerHTML = `
            <div class="admin-modal-content">
                <h3>🔗 Thay đổi đường dẫn</h3>
                <label style="font-weight:600;margin-bottom:8px;display:block;">URL đường dẫn:</label>
                <input type="text" id="link-url-input" placeholder="Nhập URL mới (VD: /products?category=NỮ)" value="${currentHref}">
                <p style="font-size:12px;color:#666;margin-top:8px;">
                    <strong>Gợi ý:</strong><br>
                    - Trang sản phẩm: /products<br>
                    - Lọc theo danh mục: /products?category=NỮ<br>
                    - Lọc theo danh mục: /products?category=NAM<br>
                    - Lọc bé gái: /products?category=BÉ GÁI<br>
                    - Lọc bé trai: /products?category=BÉ TRAI
                </p>
                <div class="admin-modal-buttons">
                    <button class="btn-cancel">Hủy</button>
                    <button class="btn-save">Áp dụng</button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        const urlInput = document.getElementById('link-url-input');

        modal.querySelector('.btn-cancel').onclick = () => modal.remove();
        modal.querySelector('.btn-save').onclick = () => {
            const newHref = urlInput.value;
            if (newHref && newHref !== currentHref) {
                linkElement.href = newHref;
                linkElement.classList.add('changed');
                changedElements.set(key, { type: 'link', element: linkElement, value: newHref });
                updateStatus();
                showToast('✅ Đã cập nhật đường dẫn!', 'success');
            }
            modal.remove();
        };
        modal.onclick = (e) => {
            if (e.target === modal) modal.remove();
        };
    }

    function showImageModal(key, currentSrc, imgElement) {
        const modal = document.createElement('div');
        modal.className = 'admin-modal';
        modal.innerHTML = `
            <div class="admin-modal-content">
                <h3>📷 Thay đổi hình ảnh</h3>
                <div class="admin-modal-preview">
                    <img src="${currentSrc}" id="preview-image" alt="Preview">
                </div>
                <label style="font-weight:600;margin-bottom:8px;display:block;">URL hình ảnh:</label>
                <input type="text" id="image-url-input" placeholder="Nhập URL hình ảnh mới" value="${currentSrc}">
                <label style="font-weight:600;margin-bottom:8px;display:block;">Hoặc tải lên từ máy tính:</label>
                <input type="file" id="image-file-input" accept="image/*">
                <div class="admin-modal-buttons">
                    <button class="btn-cancel">Hủy</button>
                    <button class="btn-save">Áp dụng</button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        const urlInput = document.getElementById('image-url-input');
        const fileInput = document.getElementById('image-file-input');
        const previewImg = document.getElementById('preview-image');

        // Preview URL changes
        urlInput.addEventListener('input', () => {
            previewImg.src = urlInput.value;
        });

        // Handle file upload
        fileInput.addEventListener('change', async (e) => {
            const file = e.target.files[0];
            if (file) {
                const formData = new FormData();
                formData.append('file', file);

                try {
                    const response = await fetch('/api/content/upload-image', {
                        method: 'POST',
                        body: formData
                    });
                    const result = await response.json();
                    if (result.success) {
                        urlInput.value = result.url;
                        previewImg.src = result.url;
                        showToast('✅ Đã tải lên thành công!', 'success');
                    } else {
                        showToast('❌ Lỗi: ' + result.message, 'error');
                    }
                } catch (error) {
                    // Fallback: use data URL
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        urlInput.value = e.target.result;
                        previewImg.src = e.target.result;
                    };
                    reader.readAsDataURL(file);
                }
            }
        });

        modal.querySelector('.btn-cancel').onclick = () => modal.remove();
        modal.querySelector('.btn-save').onclick = () => {
            const newSrc = urlInput.value;
            if (newSrc && newSrc !== currentSrc) {
                imgElement.src = newSrc;
                imgElement.classList.add('changed');
                changedElements.set(key, { type: 'image', element: imgElement, value: newSrc });
                updateStatus();
            }
            modal.remove();
        };
        modal.onclick = (e) => {
            if (e.target === modal) modal.remove();
        };
    }

    function updateStatus() {
        const status = document.getElementById('edit-status');
        const count = changedElements.size;
        if (count > 0) {
            status.textContent = `${count} thay đổi chưa lưu`;
            status.style.color = '#FF9800';
        } else {
            status.textContent = 'Không có thay đổi';
            status.style.color = '#4CAF50';
        }
    }

    // Save all changes
    async function saveAllChanges() {
        if (changedElements.size === 0) {
            showToast('Không có thay đổi nào để lưu', 'info');
            return;
        }

        const status = document.getElementById('edit-status');
        status.textContent = 'Đang lưu...';

        const updates = [];
        changedElements.forEach((data, key) => {
            updates.push({ 
                key, 
                value: data.value,
                type: data.type
            });
        });

        try {
            const response = await fetch('/api/content/bulk-update', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updates)
            });

            const result = await response.json();

            if (result.success) {
                showToast('✅ ' + result.message, 'success');
                
                // Mark elements as saved
                changedElements.forEach((data, key) => {
                    if (data.element) {
                        data.element.classList.remove('changed');
                        data.element.classList.add('saved');
                        setTimeout(() => data.element.classList.remove('saved'), 500);
                    }
                });

                // Update original values
                changedElements.forEach((data, key) => {
                    originalValues.set(key, data.value);
                });

                changedElements.clear();
                updateStatus();
            } else {
                showToast('❌ Lỗi: ' + result.message, 'error');
            }
        } catch (error) {
            console.error('Save error:', error);
            showToast('❌ Lỗi kết nối server', 'error');
        }
    }

    // Cancel changes
    function cancelChanges() {
        if (changedElements.size > 0) {
            if (!confirm('Bạn có chắc muốn hủy tất cả thay đổi?')) {
                return;
            }
        }

        // Restore original values
        changedElements.forEach((data, key) => {
            if (data.element) {
                if (data.type === 'text') {
                    data.element.innerHTML = originalValues.get(key);
                } else if (data.type === 'image') {
                    data.element.src = originalValues.get(key);
                }
                data.element.classList.remove('changed');
            }
        });

        changedElements.clear();
        disableEditMode();
        showToast('Đã hủy tất cả thay đổi', 'info');
    }

    // Toast notification
    function showToast(message, type = 'info') {
        // Remove existing toasts
        document.querySelectorAll('.admin-toast').forEach(t => t.remove());
        
        const toast = document.createElement('div');
        toast.className = 'admin-toast';
        toast.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 25px;
            background: ${type === 'success' ? '#4CAF50' : type === 'error' ? '#f44336' : '#2196F3'};
            color: white;
            border-radius: 8px;
            z-index: 100001;
            font-size: 14px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
            animation: slideIn 0.3s ease;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        `;
        toast.textContent = message;
        document.body.appendChild(toast);

        setTimeout(() => {
            toast.style.animation = 'slideOut 0.3s ease';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    // Add animation keyframes
    const animStyle = document.createElement('style');
    animStyle.textContent = `
        @keyframes slideIn {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        @keyframes slideOut {
            from { transform: translateX(0); opacity: 1; }
            to { transform: translateX(100%); opacity: 0; }
        }
    `;
    document.head.appendChild(animStyle);

    // Event listeners
    document.getElementById('toggle-edit-mode').addEventListener('click', toggleEditMode);
    document.getElementById('save-all-changes').addEventListener('click', saveAllChanges);
    document.getElementById('cancel-changes').addEventListener('click', cancelChanges);

    // Keyboard shortcuts
    document.addEventListener('keydown', (e) => {
        if (e.ctrlKey && e.key === 's' && editMode) {
            e.preventDefault();
            saveAllChanges();
        }
        if (e.key === 'Escape' && editMode) {
            cancelChanges();
        }
    });

    // Load saved content when page loads
    loadSavedContent();

    console.log('🛠️ Admin Inline Editor v2.0 loaded');
})();
