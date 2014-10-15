Getting started
========

1. Launch SBT:

        $ sbt

2. Compile everything and run all tests:

        > test

3. Start the application (will be restarted on changes in codebase. Press `Enter` to stop restarts):

        > ~re-start

4. Browse to [http://localhost:8080](http://localhost:8080/)

5. Stop the application:

        > re-stop


Debugging
========

To debug use remote debugging (socket attach to port `5005`).


Run code coverage
========

        $ sbt clean scoverage:test


Ui mockups
========

Can be found [here](https://moqups.com/eugeny.loy@gmail.com/NyiTxw4d).


Architecture overview
========

![architecture overview](/docs/architecture_overview.png)
