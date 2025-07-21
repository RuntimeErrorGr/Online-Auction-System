# Assumptions in the Implementation

During the implementation of the project, several assumptions were made to clarify the operations of the auction house:

- Clients, brokers, and products are read from separate text files and parsed into the auction house's respective lists.
- In order to begin operations, an auction house must first hire at least one broker to mediate auctions. Only then can the manager add products and accept clients to start bidding.
- The manager’s responsibilities include hiring brokers and adding products to the stock.
- Both products and clients can be loaded simultaneously, or only one of them.
- A client can bid **only** on products available in stock at the moment they contact the auction house and that are **not already part of an ongoing auction**. Products added after the client has submitted their wish list will not be available to them for bidding.
- A client has a single broker, while a broker can represent multiple clients.
- A client never offers less than the starting price of a product when bidding begins.
- A client can bid on any number of available products. Therefore, collaboration between a client and broker is not terminated after a single auction finishes, since the client might be involved in multiple ongoing auctions.
- An auction ends either after a maximum number of steps (set when the auction object is created) or if only one client remains in a round. If all clients withdraw, the product is not sold and the auction ends.
- If only one client remains in a round and places a bid without knowing others have withdrawn, the product is sold to them at the price offered **in the previous round**.
- Most numeric values are generated using `Random`, so the auction house simulation varies on each run.
- The random algorithm will **never** generate a bid higher than the client’s declared maximum budget.
- Multiple auctions can run concurrently using separate threads.
- Sold products are removed from the product list and added to a historical record.
- As specified in the project requirements, only brokers can remove products from the list. Therefore, the broker of the winning client deletes the product when an auction ends (within the corresponding auction thread).
- The auction house can still accept clients even when its stock is empty.

---

# Console Application Usage

To implement the console, the **Command** design pattern was used. This involves a `Command` interface, specific command classes, and a `Parser` class.

Available user commands:

- `hire <broker_file>`  
  Mandatory first command. The auction house cannot operate without brokers. Calls the `execute()` method of a `HireCommand` object.

- `list <products/clients/employees/auctions>`  
  Displays the requested list with all related information using a generic method from the auction house. Useful to verify the state of lists at the beginning and end of a simulation.

- `startactivity <products_file clients_file>`  
  Starts the auction simulation. The manager adds products on one thread, while the auction house receives clients on another. Auction threads are launched as needed.  
  You can load only products or only clients using `-` as a placeholder.  
  You can call `startactivity` multiple times in a session, but input files should differ to avoid duplicate exceptions.

- `quit` / `exit`  
  Exits the application.

---

# Core Functionalities

- Reading/parsing/instantiating products, clients, and brokers is done via the `Parser` class and specific `Builder` and `Factory` classes.
- The product list is synchronized using `Collections.synchronizedList`.
- Adding products: Each product is read from a file and added by the manager through a synchronized method and an `AddProductCommand` object.
- Clients view the product list and express interest in a random number of items. A broker is assigned, and auctions are updated accordingly:
  - A new auction is created if the product hasn’t been requested yet. The client is queued.
  - A client can be added to an existing auction's queue.
  - When the target number of participants is reached, the auction starts.

- Brokers remove sold products at the end of successful auctions and collect commission based on the provided rules.

---

# Auction Mechanism

Clients are represented by brokers who interact with both clients and auctions. The **Observer** design pattern is implemented:
- `Broker` is the subject for `Client`.
- `Auction` is the subject for `Broker`.
- `Broker` acts as a mediator.

When an auction reaches its participant threshold, the auction house launches an anonymous thread. In each round:
1. The auction notifies brokers of a new round and requests bids.
2. Brokers notify clients and collect their bids.
3. Clients may choose to bid (with a higher offer than last round) or withdraw (by sending -1).
4. Brokers compile bids into a `client → bid` map.
5. The highest offer is determined and a round winner is declared.  
   - If it's the final round, the round winner is also the auction winner.
   - If only one client remains, they win.

When the auction ends:
- Product is removed from the stock.
- Auction and product statuses are updated.
- Broker receives commission.
- Client’s win count is incremented.

---

# OOP Modeling and Concepts Used

1. **Encapsulation** – Responsibilities split across specific classes: `Builder`, `Factory`, `Command`, etc.
2. **Abstraction** – Abstract classes for `Client`, `Employee`, and `Product` that are extended by concrete classes. Interfaces support design patterns.
3. **Inheritance** – Concrete classes inherit and extend abstract base classes.
4. **Polymorphism** – Methods accepting base class types are passed subclass objects, e.g., `Client` → `Individual` / `JuridicalPerson`, or `Product` subclasses.
5. **Generics** – A generic `list` method is used for displaying auction house data. Observer interfaces `IObserver` and `IObservable` are also generic.
6. **Multithreading** – Implemented using `ExecutorService` with a thread pool (products thread, clients thread, and auction threads).
7. **Design Patterns**:
   - **Singleton** – `AuctionHouse` and `Manager` are single instances.
   - **Builder** – Used for concrete classes like `Painting`, `Furniture`, `Jewelry`, `Individual`, `JuridicalPerson`.
   - **Factory** – Creates concrete subclass instances from abstract classes `Product` and `Client`.
   - **Command** – `execute()` used for parsing and handling user commands.
   - **Observer** – Implements communication between:
     - `Auction` ← `Broker`
     - `Broker` ← `Client`

---
