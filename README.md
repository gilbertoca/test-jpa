test-jpa
========

Template project for any user testcase using JPA. 
To create a DataNucleus Test simply fork this project, and add/edit as necessary to add your model and persistence commands.
The files that you'll likely need to edit are

* <a href="https://github.com/datanucleus/test-jpa/tree/master/src/main/java/mydomain/model">src/main/java/mydomain/model/</a>   **[Put your model classes here]**
* <a href="https://github.com/datanucleus/test-jpa/blob/master/src/main/resources/META-INF/persistence.xml">src/main/resources/META-INF/persistence.xml</a>   **[Put your datastore details in here]**
* <a href="https://github.com/datanucleus/test-jpa/blob/master/src/test/java/org/datanucleus/test/SimpleTest.java">src/test/java/org/datanucleus/test/SimpleTest.java</a>   **[Edit this if a single-thread test is required]**
* <a href="https://github.com/datanucleus/test-jpa/blob/master/src/test/java/org/datanucleus/test/MultithreadTest.java">src/test/java/org/datanucleus/test/MultithreadTest.java</a>   **[Edit this if a multi-thread test is required]**

To run this, simply type "mvn clean compile test"
