**Important: Don't forget to update the [Candidate README](#candidate-readme) section**

Real-time Transaction Challenge
===============================
## Overview
Welcome to Current's take-home technical assessment for backend engineers! We appreciate you taking the time to complete this, and we're excited to see what you come up with.

You are tasked with building a simple bank ledger system that utilizes the [event sourcing](https://martinfowler.com/eaaDev/EventSourcing.html) pattern to maintain a transaction history. The system should allow users to perform basic banking operations such as depositing funds, withdrawing funds, and checking balances. The ledger should maintain a complete and immutable record of all transactions, enabling auditability and reconstruction of account balances at any point in time.

## Details
The [included service.yml](service.yml) is the OpenAPI 3.0 schema to a service we would like you to create and host.

The service accepts two types of transactions:
1) Loads: Add money to a user (credit)

2) Authorizations: Conditionally remove money from a user (debit)

Every load or authorization PUT should return the updated balance following the transaction. Authorization declines should be saved, even if they do not impact balance calculation.


Implement the event sourcing pattern to record all banking transactions as immutable events. Each event should capture relevant information such as transaction type, amount, timestamp, and account identifier.
Define the structure of events and ensure they can be easily serialized and persisted to a data store of your choice. We do not expect you to use a persistent store (you can you in-memory object), but you can if you want. We should be able to bootstrap your project locally to test.

## Expectations
We are looking for attention in the following areas:
1) Do you accept all requests supported by the schema, in the format described?

2) Do your responses conform to the prescribed schema?

3) Does the authorizations endpoint work as documented in the schema?

4) Do you have unit and integrations test on the functionality?

Here’s a breakdown of the key criteria we’ll be considering when grading your submission:

**Adherence to Design Patterns:** We’ll evaluate whether your implementation follows established design patterns such as following the event sourcing model.

**Correctness**: We’ll assess whether your implementation effectively implements the desired pattern and meets the specified requirements.

**Testing:** We’ll assess the comprehensiveness and effectiveness of your test suite, including unit tests, integration tests, and possibly end-to-end tests. Your tests should cover critical functionalities, edge cases, and potential failure scenarios to ensure the stability of the system.

**Documentation and Clarity:** We’ll assess the clarity of your documentation, including comments within the code, README files, architectural diagrams, and explanations of design decisions. Your documentation should provide sufficient context for reviewers to understand the problem, solution, and implementation details.

# Candidate README
## Bootstrap instructions
In order to run the application, you must first have [Apache Maven](https://maven.apache.org) which is a software project management and comprehension tool. If you do not, then please go ahead and install it on your machine first. Once you have installed Maven, then you can clone this repository using
the method of your choice.

After doing the above, open a terminal and cd into the repository. Now, type the following command:

```
mvn clean package install spring-boot:run
```

This will take a couple of seconds, but when you stop receiving output and see "Started Application in X seconds" at the bottom
of your terminal output, then the service is up and running and you can access the endpoints at "http://localhost:8080/{endpoint}"

## Design considerations
*Replace this: I decided to build X for Y reasons.*

When approaching the service, I thought about providing the specified functionality but also building the service in a way that it is reliable, scalable and has good performance. That is why I constructed it using Spring Boot, a framework that is great for making web services since it aids in abstracting the very granular tasks and allows for implementation of macro level features. This would make it very easy to build upon the existing service and add more features since it provides a structure to do so.

Additionally, while considering the idea of expansion and adaptability, I built the service to follow the flow of a layered design pattern. That way, a new feature could be easily integrated if it is broken down into different layers. This added a degree of modularity to the service, aiding in that goal. This led to me following a very common architecture for RESTful API services: having a controller layer, service layer, and finally a repository layer (if necessary). 

The communication between these layers was done using objects that matched the specific schemas, thus ensuring that input-output was done as per what the service specification file (service.yml) requested making sure that it is reliable and correct performance. Additionally, given the modularity and design scheme, this allowed for testing on a much more granular level which also helped in ensuring that it is reliable.

I also grouped the features in a manner that if there was a need to build out more specific user functionality and would want to implement reconstruction of an account state up to a certain point the structure was there to do so and could be done pretty easily.

When focusing on storing data, I opted to have two different data stores. For the users, I chose to use an in-memory store for faster access and given that it could be much more likely that someone would want to know their current balance more often than their balance at a specific point in time. Also, the expansion of that data was not at such a rate that it would require a client-server database just yet. Thus the reading and writing would be done very quickly ensuring performance. On the other hand, I opted for a more scalable solution when thinking about storing the transactions. Given the nature of the service, it was much more likely that this would accumulate much faster and take up more space quicker, as such there needed to be a scalable solution to this. I ended up opting for SQLite database since it is self-contained, severless, and transactional allowing it to be easy to setup and deploy while still being persistent, scalable and fast. This would mean that even if users are stored in an in-memory object they could be reconstructed using the event storage thus making them persistent in some regard. Also, given the way that the project is setup and using Spring Boot, it is very simple to switch solutions given the support Spring Boot provides making it very plug and play.

Getting a bit more specific, I stored user balances as a map from currencies to make sure that different currency transactions are supported to some extent without fully depending on external context such as conversion rates. However, it is still written a way that if there are provided rates than the different amounts for the various currencies stored for the user can be easily converted and stored as a singular value. 



## Assumptions
One assumption that I made was that the userId is unique and an identifying element of a User. The service is implemented such that trying to perform loads and authorizations with two different users with the same userId would not be possible. I also assumed that when a load request is passed in for a user and they don't exist yet they should be automatically generated with an overall balance of 0 to start with.

On the flip side, I made the assumption that it could be that case that the message id associated with a request to the service is not unique.

Another assumption made was that event sourcing would be necessary for core functionality of the service. As such, the ping feature of the service was not implemented following the pattern.

I also assumed that this service should deal with different currencies itself in some manner but be set up in a way that if additional information is provided, such as conversion rates, user balance could be stored as one singular balance in a chosen currency.


## Bonus: Deployment considerations
Given a service that has a REST API nature such as this one and persists data, most commonly it would be deployed in an environment that can support
these characteristics. Typically, you would deploy a service such as this on the cloud, a VM, or a physical server itself. In particular, if I were to
deploy this service I would first package it using Docker so that its a single container that can be run anywhere which gives me flexibility. Then, depending on my use cases and importance, I can find the best fit for deployment. I would prefer to deploy it on the cloud using platforms such as Google Cloud, AWS, or Azure since they give ease and flexibility in deployment. In particular, I would choose to deploy on AWS using EC2. Observe below:

![diagram of deployment on aws](images/aws.jpg)

*This image is not my own creation, it is a thumbnail for a [video](https://www.youtube.com/watch?app=desktop&v=z7_LdCVnCRU).*

## License

At CodeScreen, we strongly value the integrity and privacy of our assessments. As a result, this repository is under exclusive copyright, which means you **do not** have permission to share your solution to this test publicly (i.e., inside a public GitHub/GitLab repo, on Reddit, etc.). <br>

## Submitting your solution

Please push your changes to the `main branch` of this repository. You can push one or more commits. <br>

Once you are finished with the task, please click the `Submit Solution` link on <a href="https://app.codescreen.com/candidate/f0425321-3c8b-49af-91b1-12993bc5950b" target="_blank">this screen</a>.