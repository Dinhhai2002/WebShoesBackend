-- Bảng exchange_requests
CREATE TABLE exchange_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    return_request_id INT NOT NULL,
    exchange_reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'PROCESSING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    admin_notes TEXT,
    price_difference DECIMAL(10,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (return_request_id) REFERENCES return_requests(id) ON DELETE CASCADE
);

-- Bảng exchange_request_details
CREATE TABLE exchange_request_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exchange_request_id INT NOT NULL,
    old_product_id INT NOT NULL,
    old_product_detail_id INT NOT NULL,
    new_product_id INT,
    new_product_detail_id INT,
    quantity INT NOT NULL,
    exchange_reason TEXT,
    condition_description TEXT,
    images JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (exchange_request_id) REFERENCES exchange_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (old_product_id) REFERENCES products(id),
    FOREIGN KEY (old_product_detail_id) REFERENCES product_details(id),
    FOREIGN KEY (new_product_id) REFERENCES products(id),
    FOREIGN KEY (new_product_detail_id) REFERENCES product_details(id)
);

-- Indexes cho performance
CREATE INDEX idx_exchange_requests_return_request_id ON exchange_requests(return_request_id);
CREATE INDEX idx_exchange_requests_status ON exchange_requests(status);
CREATE INDEX idx_exchange_request_details_exchange_request_id ON exchange_request_details(exchange_request_id);
CREATE INDEX idx_exchange_request_details_old_product_id ON exchange_request_details(old_product_id);
CREATE INDEX idx_exchange_request_details_old_product_detail_id ON exchange_request_details(old_product_detail_id);
CREATE INDEX idx_exchange_request_details_new_product_id ON exchange_request_details(new_product_id);
CREATE INDEX idx_exchange_request_details_new_product_detail_id ON exchange_request_details(new_product_detail_id); 