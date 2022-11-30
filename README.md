# Event Calculator

## What is this?

This project is a tool to calculate statistical parameters for complex probabilistic events. But what does that mean in practice?

Imagine you are playing a board game and you need to roll two sixes and two fives, over as few dice rolls as possible. How many dice rolls will you need on average to get this result? This isn't a trivial question, as it might seem at first glance, and solving it requires an analysis too complicated to do by hand. This tool will allow you to solve these kinds of problems automatically, using a recursive algorithm to search through all the possible rolls in an optimized manner.

## How does it work?

From a technical point of view, this is a web server that provides a REST API using Java and Spring. This API gives access to the tools provided here.

From a mathematical point of view, the algorithm used considers the different states the system can be in (how many dice rolls I have already got) and sets relations for the statistical parameters between the different states. It then works backwards from the simplest states (for example, once you already have all the dice rolls you need) to get the desired result.

## How to use this tool

Ideally, this project would be deployed as a web server on some kind of cloud service, and be available through a graphical interface that accesses the REST API.

In practice, this project isn't currently deployed anywhere, which means you have to start up the server in your own computer. To do this:

- Clone the project and import it in an IDE of your choice as a Maven project.
- Install the necessary dependencies using Maven (the exact command depends on the IDE used).
- Run the server through its main method in the class EventCalculatorApplication.
- No additional configuration (environment variables, databases, etc.) is needed to run this project.