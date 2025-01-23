# Waste Sorting and Recycling Management Application

This project is designed to promote environmental sustainability by assisting users in managing waste more effectively. The application includes the following features:

---

## Features
1. **Category Management**
   - Organize and manage various waste categories for better sorting.
2. **Disposal Tracking**
   - Facilitate proper waste disposal wit location-based recommendations.
3. **Recycling Tips**
   - Provide practical guidance on recycling practices to reduce waste and encourage reuse.
4. **Waste Item Management**
   - Maintain detailed records of waste items, their classification, and appropriate disposal methods.
5. **Global Exception Handling**
    - Robust error handling to ensure user-friendly responses for:
      - Invalid input
      - Duplicate entries 
      - Missing resources 

---

## Objective
The goal of the application is to empower users to make eco-conscious decisions, improve waste management practices, and contribute to a cleaner, more sustainable environment.

## Technology Stack
- **Backend**: Spring Boot (RESTful APIs, Validation, Exception Handling)
- **Database**: Relational Database (H2-Memory Database)
- **Validation**: Bean Validation (Hibernate Validator)

---

## End-points
### Category
- **GET** - `/api/categories` (Retrieves all categories)
- **GET** - `/api/categories/{id}` (Retrieves a category by the specified id)
- **GET** - `/api/categories/recycling-tips` (Retrieves all categories along with their respective recycling tips)
- **GET** - `/api/categories/{id}/recycling-tips` (Retrieves a category with their respective recycling tips by the specified id)
- **POST** - `/api/categories` (Create a new category)
- **PUT** - `/api/categories/{id}` (Update an existing category by the specified id)
- **DEL**  - `/api/categories/{id}` (Delete an existing category)

### Waste
- **GET** - `/api/waste` (Retrieves all waste items)
- **GET** - `/api/waste/{id}` (Retrieves a waste item by the specified id)
- **GET** - `/api/waste/overview` (Retrieves all waste items along with their category details and disposal details)
- **GET** - `/api/waste/overview?category={category_name}` (Filters through the waste items based on the specified category name)
- **GET** - `/api/waste/recycling-tips` (Retrieves all waste items with their respective recycling tips)
- **GET** - `/api/waste/{id}/recycling-tips` (Retrieves a waste item along with their recycling tips by the specified id)
- **POST** - `/api/waste` (Create a new waste item)
- **PUT** - `/api/waste/{id}` (Updates an already existing waste item)
- **DEL** - `/api/waste`/{id}` (Delete an existing waste item)

### Disposals
- **GET** - `/api/disposals` (Retrieves all disposal guidelines)
- **GET** - `/api/disposals/{id}` (Retrieves disposal guidelines for a specified waste by its id)
- **POST** - `/api/disposals` (Creates a disposal guideline for a specific waste item)
- **PUT** - `/api/disposals/{id}` (Updates a disposal guideline for a specific waste item)
- **DEL** - `/api/disposals/{id}` (Delete a disposal guideline)

### Recycling Tips
- **GET** - `/api/recycling-tips` (Retrieves all recycling tips)
- **GET** - `/api/recycling-tips/{id}` (Retrieves recycling tips by the specified id)
- **POST** - `/api/recycling-tips` (Create a new recycling tip for a category or waste item)
- **PUT** - `/api/recycling-tips/{id}` (Updates a recycling tip by its id)
- **DEL** - `/api/recycling-tips/{id}` (Delete an existing recycling tip by its id)

---
Owner: Top Dawg