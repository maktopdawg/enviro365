INSERT INTO Category ( name, description, lastUpdated )
VALUES
    ('Plastic', 'Non-biodegradable materials such as bottles, packaging, and containers.', CURRENT_TIMESTAMP ),
    ('Organic', 'Biodegradable waste such as food scraps, yard waste, and paper towels.', CURRENT_TIMESTAMP ),
    ('Glass', 'Waste made of glass such as bottles, jars, and windows.', CURRENT_TIMESTAMP ),
    ('Metal', 'Waste made of metals such as cans, foils, and scrap metals.', CURRENT_TIMESTAMP ),
    ('E-waste', 'Electronic waste such as old computers, mobile phones, and batteries.', CURRENT_TIMESTAMP );


INSERT INTO Waste ( name, description, categoryId, lastUpdated )
VALUES
    ('Plastic Bottle', 'A disposable plastic bottle often used for water and beverages.', 1, CURRENT_TIMESTAMP ),
    ('Plastic Bag', 'A lightweight plastic bag commonly used for carrying groceries.', 1, CURRENT_TIMESTAMP ),
    ('Fruit Peels', 'Organic waste from fruits such as bananas, apples, and oranges.', 2, CURRENT_TIMESTAMP ),
    ('Grass Clippings', 'Organic yard waste from mowing the lawn.', 2, CURRENT_TIMESTAMP) ,
    ('Glass Jar', 'A reusable glass container often used for storing food.', 3, CURRENT_TIMESTAMP ),
    ('Window Pane', 'A sheet of glass used in windows and often recycled into new glass products.', 3, CURRENT_TIMESTAMP ),
    ('Aluminum Can', 'A recyclable metal container used for beverages.', 4, CURRENT_TIMESTAMP ),
    ('Metal Foil', 'Thin metal sheet often used in food packaging.', 4, CURRENT_TIMESTAMP ),
    ('Old Smartphone', 'A discarded electronic device that contains valuable metals.', 5, CURRENT_TIMESTAMP ),
    ('Laptop Battery', 'An electronic waste item that must be disposed of responsibly.', 5, CURRENT_TIMESTAMP );

INSERT INTO Disposal (wasteId, method, instructions, location, lastUpdated)
VALUES
    (1, 'Recycle', 'Place in the plastic recycling bin at your local recycling center.', 'Community Recycling Center', CURRENT_TIMESTAMP),
    (2, 'Reuse', 'Reuse for storage or as trash liners before recycling.', 'Home', CURRENT_TIMESTAMP),
    (3, 'Compost', 'Add to your backyard compost bin or local composting facility.', 'Backyard Compost Bin', CURRENT_TIMESTAMP),
    (4, 'Compost', 'Take to the nearest community garden compost site.', 'Community Garden', CURRENT_TIMESTAMP),
    (5, 'Recycle', 'Rinse and place in glass recycling bin.', 'Glass Recycling Station', CURRENT_TIMESTAMP),
    (6, 'Recycle', 'Take to a designated glass recycling facility.', 'City Recycling Depot', CURRENT_TIMESTAMP),
    (7, 'Recycle', 'Crush and place in the metal recycling bin.', 'Community Recycling Center', CURRENT_TIMESTAMP),
    (8, 'Recycle', 'Fold and drop off at a scrap metal collection point.', 'Scrap Metal Yard', CURRENT_TIMESTAMP),
    (9, 'E-Waste Disposal', 'Bring to an authorized e-waste recycling facility.', 'E-Waste Recycling Facility', CURRENT_TIMESTAMP),
    (10, 'Special Collection', 'Drop off at a hazardous waste collection site.', 'Hazardous Waste Center', CURRENT_TIMESTAMP);
