CREATE DATABASE IF NOT EXISTS app;

CREATE TABLE IF NOT EXISTS `User` (
  `id` varchar(50) PRIMARY KEY,
  `username` varchar(255) NOT NULL UNIQUE,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `address` text,
  `role` boolean default 0,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `Product` (
  `id` varchar(50) PRIMARY KEY,
  `name` varchar(255) NOT NULL UNIQUE,
  `description` text,
  `category` varchar(10),
  `status` ENUM('ACTIVE', 'INACTIVE'),
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `Product_variants` (
  `id_product_variant` varchar(50) PRIMARY KEY,
  `id_product` varchar(50) NOT NULL,
  `code_product_variant` varchar(50) UNIQUE NOT NULL,
  `color` varchar(20) NOT NULL,
  `size` varchar(2) NOT NULL,
  `quantity` int,
  `image_url` text,
  `price` DECIMAL(10,2) NOT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `Cart` (
  `id_product_variant` varchar(50),
  `id_user` varchar(50),
  `quantity` int NOT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_product_variant`, `id_user`)
);

CREATE TABLE IF NOT EXISTS `Order` (
  `id` varchar(50) PRIMARY KEY,
  `id_user` varchar(50) NOT NULL,
  `total` DECIMAL(10,2) NOT NULL,
  `status` varchar(10) DEFAULT 'Pending',
  `shipping_fee` DECIMAL(10,2),
  `payment_method` ENUM('TIEN_MAT', 'CHUYEN_KHOAN'),
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `Order_items` (
  `id_order` varchar(50),
  `id_product_variant` varchar(50),
  `quantity` int NOT NULL,
  PRIMARY KEY (`id_order`, `id_product_variant`)
);

ALTER TABLE `Order` ADD FOREIGN KEY (`id_user`) REFERENCES `User` (`id`);
ALTER TABLE `Order_items` ADD FOREIGN KEY (`id_order`) REFERENCES `Order` (`id`);
ALTER TABLE `Cart` ADD FOREIGN KEY (`id_user`) REFERENCES `User` (`id`);
ALTER TABLE `Product_variants` ADD FOREIGN KEY (`id_product`) REFERENCES `Product` (`id`);
ALTER TABLE `Cart` ADD FOREIGN KEY (`id_product_variant`) REFERENCES `Product_variants` (`id_product_variant`);
ALTER TABLE `Order_items` ADD FOREIGN KEY (`id_product_variant`) REFERENCES `Product_variants` (`id_product_variant`);

INSERT IGNORE  INTO Product (id, name, description, category, status)
VALUES
('P001', 'Áo Thun Cổ Tròn Essential', 'Áo thun cotton co giãn, thấm hút tốt, form regular fit.', 'Áo', 'ACTIVE'),
('P002', 'Áo Polo Nam Basic', 'Áo polo chất liệu pique cao cấp, cổ bẻ gọn gàng.', 'Áo', 'ACTIVE'),
('P003', 'Áo Sơ Mi Oxford Trơn', 'Áo sơ mi vải oxford, kiểu dáng thanh lịch.', 'Áo', 'ACTIVE'),
('P004', 'Áo Khoác Gió Lightweight', 'Áo khoác nhẹ chống gió, phù hợp cho mùa thu.', 'Áo', 'ACTIVE'),
('P005', 'Áo Hoodie Form Rộng', 'Hoodie unisex form rộng, chất nỉ dày dặn.', 'Áo', 'ACTIVE'),
('P006', 'Áo Len Cổ Tim Classic', 'Áo len cổ tim, sợi cotton pha mềm mịn.', 'Áo', 'ACTIVE'),
('P007', 'Áo Sơ Mi Denim Nam', 'Áo sơ mi denim xanh nhạt, phong cách casual.', 'Áo', 'ACTIVE'),
('P008', 'Áo Khoác Dạ Ngắn', 'Áo dạ ngắn kiểu Hàn, form đứng sang trọng.', 'Áo', 'ACTIVE'),
('P009', 'Áo Thun Oversize Street', 'Áo thun oversize phong cách đường phố.', 'Áo', 'ACTIVE'),
('P010', 'Áo Polo Kẻ Sọc Slimfit', 'Áo polo slimfit kẻ sọc, tạo cảm giác thể thao.', 'Áo', 'ACTIVE'),
('P011', 'Quần Jean Slimfit Xanh Đậm', 'Quần jean denim co giãn nhẹ, form slimfit.', 'Quần', 'ACTIVE'),
('P012', 'Quần Tây Classic', 'Quần tây chất vải wool blend, kiểu dáng thanh lịch.', 'Quần', 'ACTIVE'),
('P013', 'Quần Jogger Thể Thao', 'Jogger thun co giãn, thích hợp hoạt động ngoài trời.', 'Quần', 'ACTIVE'),
('P014', 'Quần Kaki Ống Đứng', 'Quần kaki nam cơ bản, form ống đứng.', 'Quần', 'ACTIVE'),
('P015', 'Quần Short Cotton Basic', 'Quần short cotton mềm mại, thoải mái khi mặc.', 'Quần', 'ACTIVE'),
('P016', 'Quần Dài Linen', 'Quần dài linen mát mẻ, phù hợp mùa hè.', 'Quần', 'ACTIVE'),
('P017', 'Quần Jeans Rách Gối', 'Quần jeans phong cách streetwear, có rách nhẹ.', 'Quần', 'ACTIVE'),
('P018', 'Quần Ống Suông Vintage', 'Quần ống suông vải dày vừa, cảm hứng retro.', 'Quần', 'ACTIVE'),
('P019', 'Quần Cargo Túi Hộp', 'Quần cargo túi hộp tiện dụng, phong cách quân đội.', 'Quần', 'ACTIVE'),
('P020', 'Quần Jogger Dạ Ấm', 'Jogger vải dạ giữ ấm, phù hợp mùa đông.', 'Quần', 'ACTIVE'),
('P021', 'Áo Thun Polo Form Rộng', 'Áo thun polo cotton form rộng, thoải mái.', 'Áo', 'ACTIVE'),
('P022', 'Áo Sơ Mi Kẻ Ca Rô', 'Áo sơ mi vải poplin kẻ ca rô, phong cách smart-casual.', 'Áo', 'ACTIVE'),
('P023', 'Áo Khoác Jean Biker', 'Áo khoác denim kiểu biker, phối khóa kim loại.', 'Áo', 'ACTIVE'),
('P024', 'Áo Hoodie Zipper', 'Hoodie có khóa kéo toàn thân, chất nỉ nhẹ.', 'Áo', 'ACTIVE'),
('P025', 'Áo Len Gân Dọc', 'Áo len dệt gân dọc, mềm mại và co giãn.', 'Áo', 'ACTIVE'),
('P026', 'Áo Vest Không Tay', 'Áo gilet (vest không tay) len mỏng, phối layer.', 'Áo', 'ACTIVE'),
('P027', 'Áo Sơ Mi Linen Kẻ', 'Sơ mi linen kẻ sọc nhẹ, thoáng mát mùa hè.', 'Áo', 'ACTIVE'),
('P028', 'Áo Khoác Bomber Satin', 'Jacket bomber vải satin bóng nhẹ, form ôm hông.', 'Áo', 'ACTIVE'),
('P029', 'Áo Polo Cổ Nhật', 'Polo cổ Nhật (mao cổ), phong cách hiện đại.', 'Áo', 'ACTIVE'),
('P030', 'Áo Thun Tay Lỡ Oversize', 'Áo thun tay lỡ oversize, phong cách streetwear.', 'Áo', 'ACTIVE'),
('P031', 'Quần Jean Straight Basic', 'Quần jean ống đứng (straight), độ co giãn nhẹ.', 'Quần', 'ACTIVE'),
('P032', 'Quần Tây Âu Slim', 'Quần tây chất vải poly wool, dáng slim sang trọng.', 'Quần', 'ACTIVE'),
('P033', 'Quần Jogger Kaki', 'Jogger chất kaki dày, túi hộp tiện dụng.', 'Quần', 'ACTIVE'),
('P034', 'Quần Short Jean Rách', 'Short jean rách gấu, phong cách casual.', 'Quần', 'ACTIVE'),
('P035', 'Quần Linen Culottes', 'Quần linen ống rộng (culottes), thoải mái mùa hè.', 'Quần', 'ACTIVE'),
('P036', 'Quần Cargo Jogger', 'Quần cargo kết hợp jogger, ống thun gấu.', 'Quần', 'ACTIVE'),
('P037', 'Quần Tây Lưng Cao', 'Quần tây lưng cao dành cho nam/nu, form đứng.', 'Quần', 'ACTIVE'),
('P038', 'Quần Jean Flare', 'Quần jean ống loe (flare), phong cách retro hiện đại.', 'Quần', 'ACTIVE'),
('P039', 'Quần Short Chino', 'Short chino vải cotton pha, màu trung tính.', 'Quần', 'ACTIVE'),
('P040', 'Quần Jogger Nỉ Fleece', 'Jogger nỉ fleece giữ ấm, tiện cho mùa lạnh.', 'Quần', 'ACTIVE'),
('P041', 'Áo Thun Polo Dài Tay', 'Áo polo tay dài cotton co giãn.', 'Áo', 'ACTIVE'),
('P042', 'Áo Sơ Mi Oxford Kẻ', 'Áo sơ mi vải oxford kẻ sọc nhẹ.', 'Áo', 'ACTIVE'),
('P043', 'Áo Khoác Dù Phối Màu', 'Jacket dù phối màu blocks, chống nước nhẹ.', 'Áo', 'ACTIVE'),
('P044', 'Áo Hoodie Cropped', 'Hoodie ngắn gấu (cropped), style hiện đại.', 'Áo', 'ACTIVE'),
('P045', 'Áo Len Cổ Tròn Gân', 'Áo len cổ tròn, dệt gân ngang / dọc.', 'Áo', 'ACTIVE'),
('P046', 'Áo Vest Dạ Mỏng', 'Áo vest ngoài dạ mỏng, dùng làm layer.', 'Áo', 'ACTIVE'),
('P047', 'Áo Sơ Mi Chambray', 'Sơ mi chambray jean nhẹ, màu xanh nhạt.', 'Áo', 'ACTIVE'),
('P048', 'Áo Khoác Jean Oversized', 'Jacket jean oversized phong cách streetwear.', 'Áo', 'ACTIVE'),
('P049', 'Áo Thun Basic Tay Ngắn', 'Áo thun basic tay ngắn, màu trung tính.', 'Áo', 'ACTIVE'),
('P050', 'Áo Polo Pique Gradient', 'Áo polo tạo hiệu ứng gradient màu.', 'Áo', 'ACTIVE');

INSERT IGNORE INTO Product_variants 
(id_product_variant, id_product, code_product_variant, color, size, quantity, image_url, price)
VALUES
('V001', 'P001', 'TS001-BLK-M', 'Đen', 'M', 100, '/images/ao_den.jpg', 249000),
('V002', 'P001', 'TS001-WHT-L', 'Trắng', 'L', 80, '/images/ao_trang.jpg', 249000),
('V003', 'P002', 'PO002-NAV-M', 'Xanh Navy', 'M', 90, '/images/ao_xanhduong.jpg', 329000),
('V004', 'P002', 'PO002-GRY-L', 'Xám', 'L', 70, '/images/ao_xam.jpg', 329000),
('V005', 'P003', 'SM003-WHT-M', 'Trắng', 'M', 60, '/images/ao_trang.jpg', 399000),
('V006', 'P004', 'JK004-BLK-L', 'Đen', 'L', 50, '/images/ao_den.jpg', 459000),
('V007', 'P005', 'HD005-GRY-M', 'Xám', 'M', 120, '/images/ao_xam.jpg', 389000),
('V008', 'P006', 'SW006-BRN-M', 'Nâu', 'M', 85, '/images/ao_nau.jpg', 419000),
('V009', 'P007', 'DM007-BLU-L', 'Xanh Denim', 'L', 60, '/images/ao_xanhduong.jpg', 449000),
('V010', 'P008', 'CO008-GRY-M', 'Xám', 'M', 70, '/images/ao_xam.jpg', 699000),
('V011', 'P009', 'OV009-WHT-L', 'Trắng', 'L', 100, '/images/ao_trang.jpg', 269000),
('V012', 'P010', 'PO010-BLK-M', 'Đen', 'M', 90, '/images/ao_den.jpg', 359000),
('V013', 'P011', 'JN011-BLU-M', 'Xanh Đậm', 'M', 100, '/images/quan_xanhla.jpg', 459000),
('V014', 'P012', 'TR012-BLK-L', 'Đen', 'L', 60, '/images/quan_den.jpg', 499000),
('V015', 'P013', 'JG013-GRY-M', 'Xám', 'M', 110, '/images/quan_xam.jpg', 379000),
('V016', 'P014', 'KK014-KHK-L', 'Kaki', 'L', 80, '/images/quan_trang.jpg', 429000),
('V017', 'P015', 'SH015-BRN-M', 'Nâu', 'M', 100, '/images/quan_nau.jpg', 269000),
('V018', 'P016', 'LN016-BEI-M', 'Be', 'M', 90, '/images/quan_be.jpg', 399000),
('V019', 'P017', 'JN017-LBL-L', 'Xanh Nhạt', 'L', 60, '/images/quan_xanhla.jpg', 469000),
('V020', 'P018', 'VT018-BLK-M', 'Đen', 'M', 85, '/images/quan_den.jpg', 439000),
('V021', 'P019', 'CG019-OLV-M', 'Xanh Rêu', 'M', 70, '/images/quan_xanhla.jpg', 479000),
('V022', 'P020', 'JD020-GRY-L', 'Xám', 'L', 80, '/images/quan_xam.jpg', 499000),
('V023', 'P021', 'PP021-BLK-M', 'Đen', 'M', 90, '/images/ao_den.jpg', 339000),
('V024', 'P021', 'PP021-WHT-L', 'Trắng', 'L', 70, '/images/ao_trang.jpg', 339000),
('V025', 'P021', 'PP021-OLV-XL', 'Xanh Olive', 'XL', 60, '/images/ao_xanhduong.jpg', 339000),
('V026', 'P022', 'SM022-BLU-M', 'Xanh Dương', 'M', 80, '/images/ao_xanhduong.jpg', 429000),
('V027', 'P022', 'SM022-RED-L', 'Đỏ', 'L', 60, '/images/ao_do.jpg', 429000),
('V028', 'P023', 'JK023-DENIM-M', 'Denim', 'M', 50, '/images/ao_xanhduong.jpg', 559000),
('V029', 'P023', 'JK023-BLK-L', 'Đen', 'L', 45, '/images/ao_den.jpg', 559000),
('V030', 'P024', 'HD024-GRY-M', 'Xám', 'M', 100, '/images/ao_xam.jpg', 429000),
('V031', 'P024', 'HD024-BLK-L', 'Đen', 'L', 80, '/images/ao_den.jpg', 429000),
('V032', 'P025', 'SW025-BEG-M', 'Be', 'M', 90, '/images/ao_be.jpg', 459000),
('V033', 'P025', 'SW025-GRY-L', 'Xám', 'L', 70, '/images/ao_xam.jpg', 459000),
('V034', 'P026', 'VT026-GRY-M', 'Xám', 'M', 60, '/images/ao_xam.jpg', 499000),
('V035', 'P026', 'VT026-NAV-L', 'Xanh Navy', 'L', 50, '/images/ao_xanhduong.jpg', 499000),
('V036', 'P027', 'SM027-BLU-M', 'Xanh', 'M', 85, '/images/ao_xanhduong.jpg', 419000),
('V037', 'P027', 'SM027-CRM-L', 'Kem', 'L', 65, '/images/ao_trang.jpg', 419000),
('V038', 'P028', 'JK028-BLU-M', 'Xanh Navy', 'M', 70, '/images/ao_xanhduong.jpg', 549000),
('V039', 'P028', 'JK028-RED-L', 'Đỏ', 'L', 50, '/images/ao_do.jpg', 549000),
('V040', 'P029', 'PP029-WHT-M', 'Trắng', 'M', 95, '/images/ao_trang.jpg', 349000),
('V041', 'P029', 'PP029-BLU-L', 'Xanh', 'L', 75, '/images/ao_xanhduong.jpg', 349000),
('V042', 'P030', 'TS030-GRY-M', 'Xám', 'M', 110, '/images/ao_xam.jpg', 279000),
('V043', 'P030', 'TS030-BLK-L', 'Đen', 'L', 90, '/images/ao_den.jpg', 279000),
('V044', 'P031', 'JN031-IND-M', 'Indigo', 'M', 100, '/images/quan_trang.jpg', 499000),
('V045', 'P031', 'JN031-BLU-L', 'Xanh Dương', 'L', 80, '/images/quan_xanhduong.jpg', 499000),
('V046', 'P032', 'TR032-BLK-M', 'Đen', 'M', 70, '/images/quan_den.jpg', 519000),
('V047', 'P032', 'TR032-GLY-L', 'Xám Nhạt', 'L', 60, '/images/quan_xam.jpg', 519000),
('V048', 'P033', 'JG033-KHK-M', 'Kaki', 'M', 90, '/images/quan_xam.jpg', 399000),
('V049', 'P033', 'JG033-OLV-L', 'Xanh Olive', 'L', 70, '/images/quan_xanhduong.jpg', 399000),
('V050', 'P034', 'SH034-BLU-M', 'Xanh', 'M', 85, '/images/quan_xanhla.jpg', 299000),
('V051', 'P034', 'SH034-WHT-L', 'Trắng', 'L', 65, '/images/quan_trang.jpg', 299000),
('V052', 'P035', 'LN035-BEI-M', 'Be', 'M', 80, '/images/quan_be.jpg', 459000),
('V053', 'P035', 'LN035-GRY-L', 'Xám', 'L', 60, '/images/quan_xam.jpg', 459000),
('V054', 'P036', 'CG036-GRY-M', 'Xám', 'M', 75, '/images/quan_xam.jpg', 429000),
('V055', 'P036', 'CG036-OLV-L', 'Xanh Olive', 'L', 55, '/images/quan_xanhduong.jpg', 429000),
('V056', 'P037', 'TR037-BLK-M', 'Đen', 'M', 60, '/images/quan_den.jpg', 549000),
('V057', 'P037', 'TR037-NAV-L', 'Xanh Navy', 'L', 50, '/images/quan_xanhduong.jpg', 549000),
('V058', 'P038', 'JN038-LBL-M', 'Xanh Nhạt', 'M', 70, '/images/quan_xanhduong.jpg', 539000),
('V059', 'P038', 'JN038-IND-L', 'Indigo', 'L', 55, '/images/quan_xanhduong.jpg', 539000),
('V060', 'P039', 'SH039-KHK-M', 'Kaki', 'M', 95, '/images/quan_nau.jpg', 349000),
('V061', 'P039', 'SH039-NAV-L', 'Xanh Navy', 'L', 75, '/images/quan_xanhduong.jpg', 349000),
('V062', 'P040', 'JG040-BLK-M', 'Đen', 'M', 100, '/images/quan_den.jpg', 429000),
('V063', 'P040', 'JG040-GRY-L', 'Xám', 'L', 80, '/images/quan_xam.jpg', 429000),
('V064', 'P041', 'PP041-BLU-M', 'Xanh', 'M', 88, '/images/ao_xanhduong.jpg', 359000),
('V065', 'P041', 'PP041-WHT-L', 'Trắng', 'L', 68, '/images/ao_trang.jpg', 359000),
('V066', 'P042', 'SM042-GRY-M', 'Xám', 'M', 75, '/images/ao_xam.jpg', 419000),
('V067', 'P042', 'SM042-NAV-L', 'Xanh Navy', 'L', 55, '/images/ao_xanhduong.jpg', 419000),
('V068', 'P043', 'JK043-RED-M', 'Đỏ', 'M', 65, '/images/ao_do.jpg', 599000),
('V069', 'P043', 'JK043-GRN-L', 'Xanh Lá', 'L', 50, '/images/ao_xanhla.jpg', 599000),
('V070', 'P044', 'HD044-BLK-M', 'Đen', 'M', 80, '/images/ao_den.jpg', 399000),
('V071', 'P044', 'HD044-PNK-L', 'Hồng Nhạt', 'L', 60, '/images/ao_hong.jpg', 399000),
('V072', 'P045', 'SW045-CRM-M', 'Kem', 'M', 70, '/images/ao_trang.jpg', 429000),
('V073', 'P045', 'SW045-BLU-L', 'Xanh', 'L', 55, '/images/ao_xanhduong.jpg', 429000),
('V074', 'P046', 'VT046-GRY-M', 'Xám', 'M', 55, '/images/ao_xam.jpg', 499000),
('V075', 'P046', 'VT046-BLK-L', 'Đen', 'L', 45, '/images/ao_den.jpg', 499000),
('V076', 'P047', 'SM047-IND-M', 'Indigo', 'M', 80, '/images/ao_xanhduong.jpg', 449000),
('V077', 'P047', 'SM047-LBL-L', 'Xanh Nhạt', 'L', 60, '/images/ao_xanhduong.jpg', 449000),
('V078', 'P048', 'JK048-LBL-M', 'Xanh Nhạt', 'M', 70, '/images/ao_xanhduong.jpg', 559000),
('V079', 'P048', 'JK048-IND-L', 'Denim', 'L', 50, '/images/ao_xanhduong.jpg', 559000),
('V080', 'P049', 'TS049-WHT-M', 'Trắng', 'M', 100, '/images/ao_trang.jpg', 259000),
('V081', 'P049', 'TS049-GRY-L', 'Xám', 'L', 80, '/images/ao_xam.jpg', 259000),
('V082', 'P050', 'PP050-RED-M', 'Đỏ', 'M', 67, '/images/ao_do.jpg', 379000),
('V083', 'P050', 'PP050-BLU-L', 'Xanh', 'L', 55, '/images/ao_xanhduong.jpg', 379000);
