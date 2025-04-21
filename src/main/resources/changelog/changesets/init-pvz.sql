CREATE TABLE pvz.pvz (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    city TEXT NOT NULL CHECK (city IN ('Москва', 'Санкт-Петербург', 'Казань')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pvz.receptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pvz_id UUID NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('in_progress', 'closed')),
    started_at TIMESTAMP NOT NULL,
    closed_at timestamp,
    FOREIGN KEY (pvz_id) references pvz.pvz(id)
);

CREATE TABLE pvz.items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reception_id UUID NOT NULL,
    received_at TIMESTAMP NOT NULL,
    item_type TEXT NOT NULL CHECK (item_type IN ('electronics', 'clothing', 'footwear')),
    sequence INT NOT NULL,
    FOREIGN KEY (reception_id) references pvz.receptions(id)
);