CREATE DATABASE IF NOT EXISTS test;

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

INSERT IGNORE INTO User (id, username, password, name, phone, address, role)
VALUES
('U001', 'admin', 'admin', 'admin', '1213133', 'Ho Chi Minh', 0),
('U002', 'user', 'user', 'user', '1212133', 'Ho Chi Minh', 0);

INSERT IGNORE INTO Product (id, name, description, category, status)
VALUES
('P001', 'Áo Thun Basic Cotton', 'Áo thun cotton mềm, form regular, phong cách tối giản Outerity.', 'Áo', 'ACTIVE'),
('P002', 'Áo Polo Cổ Dệt Slimfit', 'Áo polo slimfit, cổ dệt bo tinh tế, chất pique cao cấp.', 'Áo', 'ACTIVE'),
('P003', 'Áo Sơ Mi Oxford Trơn', 'Áo sơ mi vải Oxford dày vừa, dễ phối cùng quần jean.', 'Áo', 'ACTIVE'),
('P004', 'Áo Khoác Bomber Satin', 'Áo khoác bomber satin bóng nhẹ, hiện đại và năng động.', 'Áo', 'ACTIVE'),
('P005', 'Áo Hoodie Form Rộng', 'Áo hoodie unisex form rộng, chất nỉ dày ấm áp.', 'Áo', 'ACTIVE'),
('P006', 'Áo Len Cổ Tròn Gân', 'Áo len dệt gân, phong cách basic và trẻ trung.', 'Áo', 'ACTIVE'),
('P007', 'Áo Thun Oversize Street', 'Áo thun oversize tay lỡ, phong cách đường phố Outerity.', 'Áo', 'ACTIVE'),
('P008', 'Áo Polo Tay Dài Classic', 'Áo polo tay dài, cổ dệt bo chắc chắn, màu trung tính.', 'Áo', 'ACTIVE'),
('P009', 'Áo Sơ Mi Kẻ Caro', 'Áo sơ mi caro chất vải poplin, trẻ trung và thanh lịch.', 'Áo', 'ACTIVE'),
('P010', 'Áo Thun Cổ Tròn Premium', 'Áo thun cổ tròn cotton pha spandex co giãn tốt.', 'Áo', 'ACTIVE'),
('P011', 'Áo Polo Minimal Form Slim', 'Áo polo slimfit cotton pha spandex, co giãn nhẹ.', 'Áo', 'ACTIVE'),
('P012', 'Áo Hoodie Zipper', 'Áo hoodie có khóa kéo, vải nỉ dày giữ ấm tốt.', 'Áo', 'ACTIVE'),
('P013', 'Áo Thun Cổ Tim Basic', 'Áo thun cổ tim dáng regular, đơn giản tinh tế.', 'Áo', 'ACTIVE'),
('P014', 'Áo Sơ Mi Linen Kẻ', 'Áo sơ mi linen thoáng mát, họa tiết kẻ sọc nhẹ.', 'Áo', 'ACTIVE'),
('P015', 'Áo Bomber Vải Dù', 'Áo bomber vải dù chống gió, phối bo gấu cổ tay.', 'Áo', 'ACTIVE'),
('P016', 'Áo Khoác Jean Lightwash', 'Áo khoác jean xanh nhạt, phong cách casual Outerity.', 'Áo', 'ACTIVE'),
('P017', 'Áo Polo Cổ Nhật', 'Polo cổ Nhật (mao cổ), hiện đại và tinh giản.', 'Áo', 'ACTIVE'),
('P018', 'Áo Len Tay Dài Basic', 'Áo len tay dài cổ tròn, vải sợi cotton pha ấm.', 'Áo', 'ACTIVE'),
('P019', 'Áo Hoodie Oversize Street', 'Hoodie oversize unisex phong cách đường phố.', 'Áo', 'ACTIVE'),
('P020', 'Áo Thun Tay Lỡ Graphic', 'Áo thun tay lỡ in hình tối giản, chất cotton mềm.', 'Áo', 'ACTIVE'),
('P021', 'Quần Tây Slimfit Classic', 'Quần tây dáng slimfit, vải wool blend cao cấp, thích hợp đi làm.', 'Quần', 'ACTIVE'),
('P022', 'Quần Jean Regular Basic', 'Quần jean form regular, denim dày vừa, dễ phối đồ.', 'Quần', 'ACTIVE'),
('P023', 'Quần Jogger Nỉ Thể Thao', 'Jogger nỉ cotton mềm, bo gấu, năng động hàng ngày.', 'Quần', 'ACTIVE'),
('P024', 'Quần Kaki Túi Hộp Utility', 'Quần kaki túi hộp phong cách streetwear hiện đại.', 'Quần', 'ACTIVE'),
('P025', 'Quần Short Linen Mùa Hè', 'Quần short linen thoáng mát, dáng basic Outerity.', 'Quần', 'ACTIVE'),
('P026', 'Quần Tây Dáng Rộng Form Hàn', 'Quần tây form wide Hàn Quốc, vải poly mịn cao cấp, dễ phối áo sơ mi.', 'Quần', 'ACTIVE'),
('P027', 'Quần Kaki Vintage', 'Quần kaki dày dặn, ống suông nhẹ, phong cách cổ điển Outerity.', 'Quần', 'ACTIVE'),
('P028', 'Quần Jogger Cargo Dây Rút', 'Jogger cargo dáng unisex, chất cotton pha spandex thoải mái.', 'Quần', 'ACTIVE'),
('P029', 'Quần Short Chino Basic', 'Short chino dáng vừa, vải co giãn nhẹ, phù hợp đi chơi mùa hè.', 'Quần', 'ACTIVE'),
('P030', 'Quần Linen Suông Be', 'Quần linen ống suông, nhẹ và thoáng, phù hợp thời trang tối giản.', 'Quần', 'ACTIVE');

INSERT IGNORE INTO Product_variants
(id_product_variant, id_product, code_product_variant, color, size, quantity, image_url, price)
VALUES
-- P001: 3 màu (Đen, Trắng, Xanh Navy)
('V001', 'P001', 'P001-BLK-S', 'Đen', 'S', 50, '/images/ao_den.jpg', 249000),
('V002', 'P001', 'P001-BLK-M', 'Đen', 'M', 60, '/images/ao_den.jpg', 249000),
('V003', 'P001', 'P001-BLK-L', 'Đen', 'L', 50, '/images/ao_den.jpg', 249000),
('V004', 'P001', 'P001-WHT-S', 'Trắng', 'S', 50, '/images/ao_trang.jpg', 249000),
('V005', 'P001', 'P001-WHT-M', 'Trắng', 'M', 60, '/images/ao_trang.jpg', 249000),
('V006', 'P001', 'P001-WHT-L', 'Trắng', 'L', 50, '/images/ao_trang.jpg', 249000),
('V007', 'P001', 'P001-NAV-S', 'Xanh Navy', 'S', 50, '/images/ao_xanhnavy.jpg', 249000),
('V008', 'P001', 'P001-NAV-M', 'Xanh Navy', 'M', 60, '/images/ao_xanhnavy.jpg', 249000),
('V009', 'P001', 'P001-NAV-L', 'Xanh Navy', 'L', 50, '/images/ao_xanhnavy.jpg', 249000),

-- P002: 4 màu (Xám, Đen, Trắng, Be)
('V010', 'P002', 'P002-GRY-S', 'Xám', 'S', 40, '/images/ao_xam.jpg', 329000),
('V011', 'P002', 'P002-GRY-M', 'Xám', 'M', 50, '/images/ao_xam.jpg', 329000),
('V012', 'P002', 'P002-GRY-L', 'Xám', 'L', 40, '/images/ao_xam.jpg', 329000),
('V013', 'P002', 'P002-BLK-S', 'Đen', 'S', 40, '/images/ao_den.jpg', 329000),
('V014', 'P002', 'P002-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 329000),
('V015', 'P002', 'P002-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 329000),
('V016', 'P002', 'P002-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 329000),
('V017', 'P002', 'P002-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 329000),
('V018', 'P002', 'P002-BEI-M', 'Be', 'M', 50, '/images/ao_be.jpg', 329000),
('V019', 'P002', 'P002-BEI-L', 'Be', 'L', 40, '/images/ao_be.jpg', 329000),

-- P003: 2 màu (Trắng, Xanh Navy)
('V020', 'P003', 'P003-WHT-S', 'Trắng', 'S', 40, '/images/ao_trang.jpg', 379000),
('V021', 'P003', 'P003-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 379000),
('V022', 'P003', 'P003-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 379000),
('V023', 'P003', 'P003-NAV-S', 'Xanh Navy', 'S', 40, '/images/ao_xanhnavy.jpg', 379000),
('V024', 'P003', 'P003-NAV-M', 'Xanh Navy', 'M', 50, '/images/ao_xanhnavy.jpg', 379000),
('V025', 'P003', 'P003-NAV-L', 'Xanh Navy', 'L', 40, '/images/ao_xanhnavy.jpg', 379000),

-- P004: 5 màu (Đen, Xám, Nâu, Be, Xanh Cỏ)
('V026', 'P004', 'P004-BLK-S', 'Đen', 'S', 40, '/images/ao_den.jpg', 459000),
('V027', 'P004', 'P004-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 459000),
('V028', 'P004', 'P004-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 459000),
('V029', 'P004', 'P004-GRY-S', 'Xám', 'S', 40, '/images/ao_xam.jpg', 459000),
('V030', 'P004', 'P004-GRY-M', 'Xám', 'M', 50, '/images/ao_xam.jpg', 459000),
('V031', 'P004', 'P004-GRY-L', 'Xám', 'L', 40, '/images/ao_xam.jpg', 459000),
('V032', 'P004', 'P004-BRN-S', 'Nâu', 'S', 40, '/images/ao_nau.jpg', 459000),
('V033', 'P004', 'P004-BRN-M', 'Nâu', 'M', 50, '/images/ao_nau.jpg', 459000),
('V034', 'P004', 'P004-BRN-L', 'Nâu', 'L', 40, '/images/ao_nau.jpg', 459000),
('V035', 'P004', 'P004-BEI-S', 'Be', 'S', 40, '/images/ao_be.jpg', 459000),
('V036', 'P004', 'P004-BEI-M', 'Be', 'M', 50, '/images/ao_be.jpg', 459000),
('V037', 'P004', 'P004-BEI-L', 'Be', 'L', 40, '/images/ao_be.jpg', 459000),

-- P005: 3 màu (Đen, Xám, Hồng)
('V039', 'P005', 'P005-BLK-S', 'Đen', 'S', 60, '/images/ao_den.jpg', 389000),
('V040', 'P005', 'P005-BLK-M', 'Đen', 'M', 60, '/images/ao_den.jpg', 389000),
('V041', 'P005', 'P005-BLK-L', 'Đen', 'L', 50, '/images/ao_den.jpg', 389000),
('V042', 'P005', 'P005-GRY-S', 'Xám', 'S', 50, '/images/ao_xam.jpg', 389000),
('V043', 'P005', 'P005-GRY-M', 'Xám', 'M', 50, '/images/ao_xam.jpg', 389000),
('V044', 'P005', 'P005-GRY-L', 'Xám', 'L', 50, '/images/ao_xam.jpg', 389000),
('V045', 'P005', 'P005-PNK-S', 'Hồng', 'S', 40, '/images/ao_hong.jpg', 389000),
('V046', 'P005', 'P005-PNK-M', 'Hồng', 'M', 50, '/images/ao_hong.jpg', 389000),
('V047', 'P005', 'P005-PNK-L', 'Hồng', 'L', 40, '/images/ao_hong.jpg', 389000),

-- P006: 2 màu (Be, Nâu)
('V048', 'P006', 'P006-BEI-S', 'Be', 'S', 50, '/images/ao_be.jpg', 419000),
('V049', 'P006', 'P006-BEI-M', 'Be', 'M', 50, '/images/ao_be.jpg', 419000),
('V050', 'P006', 'P006-BEI-L', 'Be', 'L', 40, '/images/ao_be.jpg', 419000),
('V051', 'P006', 'P006-BRN-S', 'Nâu', 'S', 50, '/images/ao_nau.jpg', 419000),
('V052', 'P006', 'P006-BRN-M', 'Nâu', 'M', 50, '/images/ao_nau.jpg', 419000),
('V053', 'P006', 'P006-BRN-L', 'Nâu', 'L', 40, '/images/ao_nau.jpg', 419000),

-- P007: 4 màu (Đen, Trắng, Xám, Đỏ)
('V054', 'P007', 'P007-BLK-S', 'Đen', 'S', 50, '/images/ao_den.jpg', 269000),
('V055', 'P007', 'P007-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 269000),
('V056', 'P007', 'P007-BLK-L', 'Đen', 'L', 50, '/images/ao_den.jpg', 269000),
('V057', 'P007', 'P007-WHT-S', 'Trắng', 'S', 40, '/images/ao_trang.jpg', 269000),
('V058', 'P007', 'P007-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 269000),
('V059', 'P007', 'P007-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 269000),
('V060', 'P007', 'P007-GRY-M', 'Xám', 'M', 40, '/images/ao_xam.jpg', 269000),
('V061', 'P007', 'P007-RED-M', 'Đỏ', 'M', 50, '/images/ao_do.jpg', 269000),
('V062', 'P007', 'P007-RED-L', 'Đỏ', 'L', 40, '/images/ao_do.jpg', 269000),

-- P008: 3 màu (Xanh Navy, Trắng, Đen)
('V063', 'P008', 'P008-NAV-S', 'Xanh Navy', 'S', 50, '/images/ao_xanhnavy.jpg', 359000),
('V064', 'P008', 'P008-NAV-M', 'Xanh Navy', 'M', 50, '/images/ao_xanhnavy.jpg', 359000),
('V065', 'P008', 'P008-NAV-L', 'Xanh Navy', 'L', 40, '/images/ao_xanhnavy.jpg', 359000),
('V066', 'P008', 'P008-WHT-S', 'Trắng', 'S', 40, '/images/ao_trang.jpg', 359000),
('V067', 'P008', 'P008-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 359000),
('V068', 'P008', 'P008-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 359000),
('V069', 'P008', 'P008-BLK-S', 'Đen', 'S', 40, '/images/ao_den.jpg', 359000),
('V070', 'P008', 'P008-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 359000),
('V071', 'P008', 'P008-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 359000),

-- P009: 2 màu (Đen, Xanh Cỏ)
('V072', 'P009', 'P009-BLK-S', 'Đen', 'S', 40, '/images/ao_den.jpg', 389000),
('V073', 'P009', 'P009-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 389000),
('V074', 'P009', 'P009-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 389000),
('V075', 'P009', 'P009-GRN-S', 'Xanh Cỏ', 'S', 40, '/images/ao_xanhco.jpg', 389000),
('V076', 'P009', 'P009-GRN-M', 'Xanh Cỏ', 'M', 50, '/images/ao_xanhco.jpg', 389000),
('V077', 'P009', 'P009-GRN-L', 'Xanh Cỏ', 'L', 40, '/images/ao_xanhco.jpg', 389000),

-- P010: 5 màu (Đen, Trắng, Hồng, Be, Xanh Navy)
('V078', 'P010', 'P010-BLK-S', 'Đen', 'S', 40, '/images/ao_den.jpg', 299000),
('V079', 'P010', 'P010-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 299000),
('V080', 'P010', 'P010-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 299000),
('V081', 'P010', 'P010-WHT-S', 'Trắng', 'S', 40, '/images/ao_trang.jpg', 299000),
('V082', 'P010', 'P010-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 299000),
('V083', 'P010', 'P010-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 299000),
('V084', 'P010', 'P010-PNK-S', 'Hồng', 'S', 40, '/images/ao_hong.jpg', 299000),
('V085', 'P010', 'P010-PNK-M', 'Hồng', 'M', 50, '/images/ao_hong.jpg', 299000),
('V086', 'P010', 'P010-PNK-L', 'Hồng', 'L', 40, '/images/ao_hong.jpg', 299000),
('V087', 'P010', 'P010-BEI-M', 'Be', 'M', 50, '/images/ao_be.jpg', 299000),
('V088', 'P010', 'P010-NAV-M', 'Xanh Navy', 'M', 50, '/images/ao_xanhnavy.jpg', 299000),

-- P011: 3 màu (Đen, Trắng, Be)
('V101', 'P011', 'P011-BLK-S', 'Đen', 'S', 50, '/images/ao_den.jpg', 329000),
('V102', 'P011', 'P011-BLK-M', 'Đen', 'M', 60, '/images/ao_den.jpg', 329000),
('V103', 'P011', 'P011-BLK-L', 'Đen', 'L', 50, '/images/ao_den.jpg', 329000),
('V104', 'P011', 'P011-WHT-S', 'Trắng', 'S', 50, '/images/ao_trang.jpg', 329000),
('V105', 'P011', 'P011-WHT-M', 'Trắng', 'M', 60, '/images/ao_trang.jpg', 329000),
('V106', 'P011', 'P011-WHT-L', 'Trắng', 'L', 50, '/images/ao_trang.jpg', 329000),
('V107', 'P011', 'P011-BEI-S', 'Be', 'S', 50, '/images/ao_be.jpg', 329000),
('V108', 'P011', 'P011-BEI-M', 'Be', 'M', 60, '/images/ao_be.jpg', 329000),
('V109', 'P011', 'P011-BEI-L', 'Be', 'L', 50, '/images/ao_be.jpg', 329000),

-- P012: 4 màu (Xám, Đen, Hồng, Xanh Navy)
('V110', 'P012', 'P012-GRY-S', 'Xám', 'S', 40, '/images/ao_xam.jpg', 389000),
('V111', 'P012', 'P012-GRY-M', 'Xám', 'M', 50, '/images/ao_xam.jpg', 389000),
('V112', 'P012', 'P012-GRY-L', 'Xám', 'L', 40, '/images/ao_xam.jpg', 389000),
('V113', 'P012', 'P012-BLK-S', 'Đen', 'S', 40, '/images/ao_den.jpg', 389000),
('V114', 'P012', 'P012-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 389000),
('V115', 'P012', 'P012-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 389000),
('V116', 'P012', 'P012-PNK-S', 'Hồng', 'S', 40, '/images/ao_hong.jpg', 389000),
('V117', 'P012', 'P012-PNK-M', 'Hồng', 'M', 50, '/images/ao_hong.jpg', 389000),
('V118', 'P012', 'P012-PNK-L', 'Hồng', 'L', 40, '/images/ao_hong.jpg', 389000),
('V119', 'P012', 'P012-NAV-S', 'Xanh Navy', 'S', 40, '/images/ao_xanhnavy.jpg', 389000),
('V120', 'P012', 'P012-NAV-M', 'Xanh Navy', 'M', 50, '/images/ao_xanhnavy.jpg', 389000),
('V121', 'P012', 'P012-NAV-L', 'Xanh Navy', 'L', 40, '/images/ao_xanhnavy.jpg', 389000),

-- P013: 2 màu (Trắng, Đen)
('V122', 'P013', 'P013-WHT-S', 'Trắng', 'S', 40, '/images/ao_trang.jpg', 269000),
('V123', 'P013', 'P013-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 269000),
('V124', 'P013', 'P013-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 269000),
('V125', 'P013', 'P013-BLK-S', 'Đen', 'S', 40, '/images/ao_den.jpg', 269000),
('V126', 'P013', 'P013-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 269000),
('V127', 'P013', 'P013-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 269000),

-- P014: 3 màu (Be, Xanh Cỏ, Xám)
('V128', 'P014', 'P014-BEI-S', 'Be', 'S', 40, '/images/ao_be.jpg', 379000),
('V129', 'P014', 'P014-BEI-M', 'Be', 'M', 50, '/images/ao_be.jpg', 379000),
('V130', 'P014', 'P014-BEI-L', 'Be', 'L', 40, '/images/ao_be.jpg', 379000),
('V131', 'P014', 'P014-GRN-S', 'Xanh Cỏ', 'S', 40, '/images/ao_xanhco.jpg', 379000),
('V132', 'P014', 'P014-GRN-M', 'Xanh Cỏ', 'M', 50, '/images/ao_xanhco.jpg', 379000),
('V133', 'P014', 'P014-GRN-L', 'Xanh Cỏ', 'L', 40, '/images/ao_xanhco.jpg', 379000),
('V134', 'P014', 'P014-GRY-S', 'Xám', 'S', 40, '/images/ao_xam.jpg', 379000),
('V135', 'P014', 'P014-GRY-M', 'Xám', 'M', 50, '/images/ao_xam.jpg', 379000),
('V136', 'P014', 'P014-GRY-L', 'Xám', 'L', 40, '/images/ao_xam.jpg', 379000),

-- P015: 5 màu (Đen, Be, Trắng, Đỏ, Xám)
('V137', 'P015', 'P015-BLK-S', 'Đen', 'S', 50, '/images/ao_den.jpg', 459000),
('V138', 'P015', 'P015-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 459000),
('V139', 'P015', 'P015-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 459000),
('V140', 'P015', 'P015-BEI-S', 'Be', 'S', 40, '/images/ao_be.jpg', 459000),
('V141', 'P015', 'P015-BEI-M', 'Be', 'M', 50, '/images/ao_be.jpg', 459000),
('V142', 'P015', 'P015-BEI-L', 'Be', 'L', 40, '/images/ao_be.jpg', 459000),
('V143', 'P015', 'P015-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 459000),
('V144', 'P015', 'P015-RED-M', 'Đỏ', 'M', 50, '/images/ao_do.jpg', 459000),
('V145', 'P015', 'P015-GRY-M', 'Xám', 'M', 50, '/images/ao_xam.jpg', 459000),

-- P016: 3 màu (Xanh Navy, Trắng, Nâu)
('V146', 'P016', 'P016-NAV-S', 'Xanh Navy', 'S', 50, '/images/ao_xanhnavy.jpg', 399000),
('V147', 'P016', 'P016-NAV-M', 'Xanh Navy', 'M', 50, '/images/ao_xanhnavy.jpg', 399000),
('V148', 'P016', 'P016-NAV-L', 'Xanh Navy', 'L', 40, '/images/ao_xanhnavy.jpg', 399000),
('V149', 'P016', 'P016-WHT-S', 'Trắng', 'S', 50, '/images/ao_trang.jpg', 399000),
('V150', 'P016', 'P016-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 399000),
('V151', 'P016', 'P016-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 399000),
('V152', 'P016', 'P016-BRN-S', 'Nâu', 'S', 50, '/images/ao_nau.jpg', 399000),
('V153', 'P016', 'P016-BRN-M', 'Nâu', 'M', 50, '/images/ao_nau.jpg', 399000),
('V154', 'P016', 'P016-BRN-L', 'Nâu', 'L', 40, '/images/ao_nau.jpg', 399000),

-- P017: 4 màu (Đen, Trắng, Xám, Xanh Navy)
('V155', 'P017', 'P017-BLK-S', 'Đen', 'S', 50, '/images/ao_den.jpg', 349000),
('V156', 'P017', 'P017-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 349000),
('V157', 'P017', 'P017-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 349000),
('V158', 'P017', 'P017-WHT-S', 'Trắng', 'S', 50, '/images/ao_trang.jpg', 349000),
('V159', 'P017', 'P017-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 349000),
('V160', 'P017', 'P017-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 349000),
('V161', 'P017', 'P017-GRY-S', 'Xám', 'S', 50, '/images/ao_xam.jpg', 349000),
('V162', 'P017', 'P017-GRY-M', 'Xám', 'M', 50, '/images/ao_xam.jpg', 349000),
('V163', 'P017', 'P017-GRY-L', 'Xám', 'L', 40, '/images/ao_xam.jpg', 349000),
('V164', 'P017', 'P017-NAV-S', 'Xanh Navy', 'S', 50, '/images/ao_xanhnavy.jpg', 349000),
('V165', 'P017', 'P017-NAV-M', 'Xanh Navy', 'M', 50, '/images/ao_xanhnavy.jpg', 349000),
('V166', 'P017', 'P017-NAV-L', 'Xanh Navy', 'L', 40, '/images/ao_xanhnavy.jpg', 349000),

-- P018: 2 màu (Nâu, Be)
('V167', 'P018', 'P018-BRN-S', 'Nâu', 'S', 50, '/images/ao_nau.jpg', 419000),
('V168', 'P018', 'P018-BRN-M', 'Nâu', 'M', 50, '/images/ao_nau.jpg', 419000),
('V169', 'P018', 'P018-BRN-L', 'Nâu', 'L', 40, '/images/ao_nau.jpg', 419000),
('V170', 'P018', 'P018-BEI-S', 'Be', 'S', 50, '/images/ao_be.jpg', 419000),
('V171', 'P018', 'P018-BEI-M', 'Be', 'M', 50, '/images/ao_be.jpg', 419000),
('V172', 'P018', 'P018-BEI-L', 'Be', 'L', 40, '/images/ao_be.jpg', 419000),

-- P019: 5 màu (Đen, Hồng, Trắng, Xanh Cỏ, Đỏ)
('V173', 'P019', 'P019-BLK-S', 'Đen', 'S', 40, '/images/ao_den.jpg', 379000),
('V174', 'P019', 'P019-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 379000),
('V175', 'P019', 'P019-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 379000),
('V176', 'P019', 'P019-PNK-S', 'Hồng', 'S', 40, '/images/ao_hong.jpg', 379000),
('V177', 'P019', 'P019-PNK-M', 'Hồng', 'M', 50, '/images/ao_hong.jpg', 379000),
('V178', 'P019', 'P019-PNK-L', 'Hồng', 'L', 40, '/images/ao_hong.jpg', 379000),
('V179', 'P019', 'P019-WHT-S', 'Trắng', 'S', 40, '/images/ao_trang.jpg', 379000),
('V180', 'P019', 'P019-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 379000),
('V181', 'P019', 'P019-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 379000),
('V182', 'P019', 'P019-GRN-S', 'Xanh Cỏ', 'S', 40, '/images/ao_xanhco.jpg', 379000),
('V183', 'P019', 'P019-RED-M', 'Đỏ', 'M', 50, '/images/ao_do.jpg', 379000),

-- P020: 3 màu (Đen, Trắng, Xám)
('V184', 'P020', 'P020-BLK-S', 'Đen', 'S', 50, '/images/ao_den.jpg', 289000),
('V185', 'P020', 'P020-BLK-M', 'Đen', 'M', 50, '/images/ao_den.jpg', 289000),
('V186', 'P020', 'P020-BLK-L', 'Đen', 'L', 40, '/images/ao_den.jpg', 289000),
('V187', 'P020', 'P020-WHT-S', 'Trắng', 'S', 50, '/images/ao_trang.jpg', 289000),
('V188', 'P020', 'P020-WHT-M', 'Trắng', 'M', 50, '/images/ao_trang.jpg', 289000),
('V189', 'P020', 'P020-WHT-L', 'Trắng', 'L', 40, '/images/ao_trang.jpg', 289000),
('V190', 'P020', 'P020-GRY-S', 'Xám', 'S', 20,  '/images/ao_xam.jpg', 289000),
('V191', 'P020', 'P020-GRY-M', 'Xám', 'M', 30,  '/images/ao_xam.jpg', 289000),
('V192', 'P020', 'P020-GRY-L', 'Xám', 'L', 10,  '/images/ao_xam.jpg', 289000),

-- P021: 4 màu (Đen, Xám, Trắng, Nâu)
('V201', 'P021', 'P021-BLK-S', 'Đen', 'S', 40, '/images/quan_den.jpg', 529000),
('V202', 'P021', 'P021-BLK-M', 'Đen', 'M', 50, '/images/quan_den.jpg', 529000),
('V203', 'P021', 'P021-BLK-L', 'Đen', 'L', 40, '/images/quan_den.jpg', 529000),
('V204', 'P021', 'P021-GRY-S', 'Xám', 'S', 40, '/images/quan_xam.jpg', 529000),
('V205', 'P021', 'P021-GRY-M', 'Xám', 'M', 50, '/images/quan_xam.jpg', 529000),
('V206', 'P021', 'P021-GRY-L', 'Xám', 'L', 40, '/images/quan_xam.jpg', 529000),
('V207', 'P021', 'P021-WHT-S', 'Trắng', 'S', 40, '/images/quan_trang.jpg', 529000),
('V208', 'P021', 'P021-WHT-M', 'Trắng', 'M', 50, '/images/quan_trang.jpg', 529000),
('V209', 'P021', 'P021-WHT-L', 'Trắng', 'L', 40, '/images/quan_trang.jpg', 529000),
('V210', 'P021', 'P021-BRN-S', 'Nâu', 'S', 40, '/images/quan_nau.jpg', 529000),
('V211', 'P021', 'P021-BRN-M', 'Nâu', 'M', 50, '/images/quan_nau.jpg', 529000),
('V212', 'P021', 'P021-BRN-L', 'Nâu', 'L', 40, '/images/quan_nau.jpg', 529000),

-- P022: 3 màu (Xanh Lá, Đen, Xám)
('V213', 'P022', 'P022-GRN-S', 'Xanh Lá', 'S', 40, '/images/quan_xanhla.jpg', 489000),
('V214', 'P022', 'P022-GRN-M', 'Xanh Lá', 'M', 50, '/images/quan_xanhla.jpg', 489000),
('V215', 'P022', 'P022-GRN-L', 'Xanh Lá', 'L', 40, '/images/quan_xanhla.jpg', 489000),
('V216', 'P022', 'P022-BLK-S', 'Đen', 'S', 40, '/images/quan_den.jpg', 489000),
('V217', 'P022', 'P022-BLK-M', 'Đen', 'M', 50, '/images/quan_den.jpg', 489000),
('V218', 'P022', 'P022-BLK-L', 'Đen', 'L', 40, '/images/quan_den.jpg', 489000),
('V219', 'P022', 'P022-GRY-S', 'Xám', 'S', 40, '/images/quan_xam.jpg', 489000),
('V220', 'P022', 'P022-GRY-M', 'Xám', 'M', 50, '/images/quan_xam.jpg', 489000),
('V221', 'P022', 'P022-GRY-L', 'Xám', 'L', 40, '/images/quan_xam.jpg', 489000),

-- P023: 5 màu (Đen, Xám, Trắng, Nâu, Xanh Lá)
('V222', 'P023', 'P023-BLK-S', 'Đen', 'S', 50, '/images/quan_den.jpg', 459000),
('V223', 'P023', 'P023-BLK-M', 'Đen', 'M', 50, '/images/quan_den.jpg', 459000),
('V224', 'P023', 'P023-BLK-L', 'Đen', 'L', 40, '/images/quan_den.jpg', 459000),
('V225', 'P023', 'P023-GRY-S', 'Xám', 'S', 40, '/images/quan_xam.jpg', 459000),
('V226', 'P023', 'P023-GRY-M', 'Xám', 'M', 50, '/images/quan_xam.jpg', 459000),
('V227', 'P023', 'P023-GRY-L', 'Xám', 'L', 40, '/images/quan_xam.jpg', 459000),
('V228', 'P023', 'P023-WHT-S', 'Trắng', 'S', 40, '/images/quan_trang.jpg', 459000),
('V229', 'P023', 'P023-WHT-M', 'Trắng', 'M', 50, '/images/quan_trang.jpg', 459000),
('V230', 'P023', 'P023-WHT-L', 'Trắng', 'L', 40, '/images/quan_trang.jpg', 459000),
('V231', 'P023', 'P023-BRN-S', 'Nâu', 'S', 40, '/images/quan_nau.jpg', 459000),
('V232', 'P023', 'P023-BRN-M', 'Nâu', 'M', 50, '/images/quan_nau.jpg', 459000),
('V233', 'P023', 'P023-BRN-L', 'Nâu', 'L', 40, '/images/quan_nau.jpg', 459000),
('V234', 'P023', 'P023-GRN-S', 'Xanh Lá', 'S', 40, '/images/quan_xanhla.jpg', 459000),
('V235', 'P023', 'P023-GRN-M', 'Xanh Lá', 'M', 50, '/images/quan_xanhla.jpg', 459000),
('V236', 'P023', 'P023-GRN-L', 'Xanh Lá', 'L', 40, '/images/quan_xanhla.jpg', 459000),

-- P024: 2 màu (Nâu, Xanh Lá)
('V237', 'P024', 'P024-BRN-S', 'Nâu', 'S', 40, '/images/quan_nau.jpg', 379000),
('V238', 'P024', 'P024-BRN-M', 'Nâu', 'M', 50, '/images/quan_nau.jpg', 379000),
('V239', 'P024', 'P024-BRN-L', 'Nâu', 'L', 40, '/images/quan_nau.jpg', 379000),
('V240', 'P024', 'P024-GRN-S', 'Xanh Lá', 'S', 40, '/images/quan_xanhla.jpg', 379000),
('V241', 'P024', 'P024-GRN-M', 'Xanh Lá', 'M', 50, '/images/quan_xanhla.jpg', 379000),
('V242', 'P024', 'P024-GRN-L', 'Xanh Lá', 'L', 40, '/images/quan_xanhla.jpg', 379000),

-- P025: 3 màu (Trắng, Xám, Đen)
('V243', 'P025', 'P025-WHT-S', 'Trắng', 'S', 40, '/images/quan_trang.jpg', 359000),
('V244', 'P025', 'P025-WHT-M', 'Trắng', 'M', 50, '/images/quan_trang.jpg', 359000),
('V245', 'P025', 'P025-WHT-L', 'Trắng', 'L', 40, '/images/quan_trang.jpg', 359000),
('V246', 'P025', 'P025-GRY-S', 'Xám', 'S', 40, '/images/quan_xam.jpg', 359000),
('V247', 'P025', 'P025-GRY-M', 'Xám', 'M', 50, '/images/quan_xam.jpg', 359000),
('V248', 'P025', 'P025-GRY-L', 'Xám', 'L', 40, '/images/quan_xam.jpg', 359000),
('V249', 'P025', 'P025-BLK-S', 'Đen', 'S', 40, '/images/quan_den.jpg', 359000),
('V250', 'P025', 'P025-BLK-M', 'Đen', 'M', 50, '/images/quan_den.jpg', 359000),
('V251', 'P025', 'P025-BLK-L', 'Đen', 'L', 40, '/images/quan_den.jpg', 359000),

-- P026: 5 màu (Đen, Xám, Trắng, Nâu, Be)
('V252', 'P026', 'P026-BLK-S', 'Đen', 'S', 40, '/images/quan_den.jpg', 589000),
('V253', 'P026', 'P026-BLK-M', 'Đen', 'M', 50, '/images/quan_den.jpg', 589000),
('V254', 'P026', 'P026-BLK-L', 'Đen', 'L', 40, '/images/quan_den.jpg', 589000),
('V255', 'P026', 'P026-GRY-S', 'Xám', 'S', 40, '/images/quan_xam.jpg', 589000),
('V256', 'P026', 'P026-GRY-M', 'Xám', 'M', 50, '/images/quan_xam.jpg', 589000),
('V257', 'P026', 'P026-GRY-L', 'Xám', 'L', 40, '/images/quan_xam.jpg', 589000),
('V258', 'P026', 'P026-WHT-S', 'Trắng', 'S', 40, '/images/quan_trang.jpg', 589000),
('V259', 'P026', 'P026-WHT-M', 'Trắng', 'M', 50, '/images/quan_trang.jpg', 589000),
('V260', 'P026', 'P026-WHT-L', 'Trắng', 'L', 40, '/images/quan_trang.jpg', 589000),
('V261', 'P026', 'P026-BRN-S', 'Nâu', 'S', 40, '/images/quan_nau.jpg', 589000),
('V262', 'P026', 'P026-BRN-M', 'Nâu', 'M', 50, '/images/quan_nau.jpg', 589000),
('V263', 'P026', 'P026-BRN-L', 'Nâu', 'L', 40, '/images/quan_nau.jpg', 589000),
('V264', 'P026', 'P026-BEI-S', 'Be', 'S', 40, '/images/quan_be.jpg', 589000),
('V265', 'P026', 'P026-BEI-M', 'Be', 'M', 50, '/images/quan_be.jpg', 589000),
('V266', 'P026', 'P026-BEI-L', 'Be', 'L', 40, '/images/quan_be.jpg', 589000),

-- P027: 3 màu (Nâu, Xanh Navy, Đen)
('V267', 'P027', 'P027-BRN-S', 'Nâu', 'S', 40, '/images/quan_nau.jpg', 499000),
('V268', 'P027', 'P027-BRN-M', 'Nâu', 'M', 50, '/images/quan_nau.jpg', 499000),
('V269', 'P027', 'P027-BRN-L', 'Nâu', 'L', 40, '/images/quan_nau.jpg', 499000),
('V270', 'P027', 'P027-NAV-S', 'Xanh Navy', 'S', 40, '/images/quan_xanhduong.jpg', 499000),
('V271', 'P027', 'P027-NAV-M', 'Xanh Navy', 'M', 50, '/images/quan_xanhduong.jpg', 499000),
('V272', 'P027', 'P027-NAV-L', 'Xanh Navy', 'L', 40, '/images/quan_xanhduong.jpg', 499000),
('V273', 'P027', 'P027-BLK-S', 'Đen', 'S', 40, '/images/quan_den.jpg', 499000),
('V274', 'P027', 'P027-BLK-M', 'Đen', 'M', 50, '/images/quan_den.jpg', 499000),
('V275', 'P027', 'P027-BLK-L', 'Đen', 'L', 40, '/images/quan_den.jpg', 499000),

-- P028: 4 màu (Xám, Xanh Lá, Nâu, Trắng)
('V276', 'P028', 'P028-GRY-S', 'Xám', 'S', 40, '/images/quan_xam.jpg', 459000),
('V277', 'P028', 'P028-GRY-M', 'Xám', 'M', 50, '/images/quan_xam.jpg', 459000),
('V278', 'P028', 'P028-GRY-L', 'Xám', 'L', 40, '/images/quan_xam.jpg', 459000),
('V279', 'P028', 'P028-GRN-S', 'Xanh Lá', 'S', 40, '/images/quan_xanhla.jpg', 459000),
('V280', 'P028', 'P028-GRN-M', 'Xanh Lá', 'M', 50, '/images/quan_xanhla.jpg', 459000),
('V281', 'P028', 'P028-GRN-L', 'Xanh Lá', 'L', 40, '/images/quan_xanhla.jpg', 459000),
('V282', 'P028', 'P028-BRN-S', 'Nâu', 'S', 40, '/images/quan_nau.jpg', 459000),
('V283', 'P028', 'P028-BRN-M', 'Nâu', 'M', 50, '/images/quan_nau.jpg', 459000),
('V284', 'P028', 'P028-BRN-L', 'Nâu', 'L', 40, '/images/quan_nau.jpg', 459000),
('V285', 'P028', 'P028-WHT-S', 'Trắng', 'S', 40, '/images/quan_trang.jpg', 459000),
('V286', 'P028', 'P028-WHT-M', 'Trắng', 'M', 50, '/images/quan_trang.jpg', 459000),
('V287', 'P028', 'P028-WHT-L', 'Trắng', 'L', 40, '/images/quan_trang.jpg', 459000),

-- P029: 2 màu (Be, Xanh Lá)
('V288', 'P029', 'P029-BEI-S', 'Be', 'S', 40, '/images/quan_be.jpg', 379000),
('V289', 'P029', 'P029-BEI-M', 'Be', 'M', 50, '/images/quan_be.jpg', 379000),
('V290', 'P029', 'P029-BEI-L', 'Be', 'L', 40, '/images/quan_be.jpg', 379000),
('V291', 'P029', 'P029-GRN-S', 'Xanh Lá', 'S', 40, '/images/quan_xanhla.jpg', 379000),
('V292', 'P029', 'P029-GRN-M', 'Xanh Lá', 'M', 50, '/images/quan_xanhla.jpg', 379000),
('V293', 'P029', 'P029-GRN-L', 'Xanh Lá', 'L', 40, '/images/quan_xanhla.jpg', 379000),

-- P030: 3 màu (Đen, Be, Xám)
('V294', 'P030', 'P030-BLK-S', 'Đen', 'S', 40, '/images/quan_den.jpg', 469000),
('V295', 'P030', 'P030-BLK-M', 'Đen', 'M', 50, '/images/quan_den.jpg', 469000),
('V296', 'P030', 'P030-BLK-L', 'Đen', 'L', 40, '/images/quan_den.jpg', 469000),
('V297', 'P030', 'P030-BEI-S', 'Be', 'S', 40, '/images/quan_be.jpg', 469000),
('V298', 'P030', 'P030-BEI-M', 'Be', 'M', 50, '/images/quan_be.jpg', 469000),
('V299', 'P030', 'P030-BEI-L', 'Be', 'L', 40, '/images/quan_be.jpg', 469000),
('V300', 'P030', 'P030-GRY-S', 'Xám', 'S', 40, '/images/quan_xam.jpg', 469000),
('V301', 'P030', 'P030-GRY-M', 'Xám', 'M', 50, '/images/quan_xam.jpg', 469000),
('V302', 'P030', 'P030-GRY-L', 'Xám', 'L', 40, '/images/quan_xam.jpg', 469000);

INSERT IGNORE INTO Cart (id_product_variant, id_user, quantity)
VALUES
('V001', 'U001', 2),
('V004', 'U001', 2),
('V006', 'U001', 2);

INSERT IGNORE INTO `Order` (id, id_user, total, shipping_fee, payment_method)
VALUES
('O001', 'U001', 249000.00, 10000.00, 'TIEN_MAT');

INSERT IGNORE INTO Order_items (id_order, id_product_variant, quantity)
VALUES
('O001', 'V001', 1);
