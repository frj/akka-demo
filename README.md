Akka Demo
=========

This is a simple demo of akka clustering. It consists of a set of compute nodes in a cluster with a client making
a request to a service provided by the compute nodes. The client uses the cluster client contrib module to avoid it
needing to be part of the cluster its self.

Usage
-----

Clone the Akka demo repository

'''
git@github.com:frj/akka-demo.git
'''

Start the cluster seed node and default receptionist using sbt:

'''
sbt "project computeNode" run
'''

Start other members of the cluster in separate terminals using unique port numbers, eg:

'''
sbt "project computeNode" "run 2552"
'''

Once the members of the compute cluster are up run the client in its own terminal:

'''
sbt "project client" run
'''

You will see the client sending a number of jobs to the cluster, each of the members processing a number of them and
the client receiving the results

