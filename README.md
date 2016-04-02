#FIDO UAF Relying Party Server

## Purpose
This is a sample FIDO UAF relying party server application written in Java and designed to run on RedHat OpenShift which uses IdentityX ([https://www.daon.com/identityX](https://www.daon.com/identityx))  to perform FIDO UAF operations.  It demonstrates how to integrate with IdentityX and use FIDO UAF Clients and Authenticators.

(Redhat OpenShift offers a free tier which will allow you to get this server deployed and available via https with a valid security certificate for free)

There is an associated FIDO UAF Relying Party Android App ([https://github.com/daoninc/fido-android-rp-app](https://github.com/daoninc/fido-android-rp-app)) which demonstrates how to interact with FIDO UAF Clients and Authenticators on Android. 

*IdentityX is a human authentication platform enabling people, across any channel to easily assert and protect their identity.*

Basic Instructions for getting this server up and running on OpenShift are below but for more details on the steps to get the sample App up and running please see [https://daoninc.github.io/fido-integration](https://daoninc.github.io/fido-integration).

##Steps to get the server up and running on RedHat OpenShift
### Prerequisite

Before we can start building the application, we need to have an OpenShift free account and client tools installed.
You will also need to have have the latest Java 1.8SE JDK installed on your dev machine.

### Step 1: Create DIY application

To create an application using client tools, type the following command:

    rhc app create fidorpsa diy-0.1

This command creates an application *fidorpsa* using *DIY* cartridge and clones the repository to *fidorpsa* directory.

### Step 2: Delete Template Application Source code

OpenShift creates a template project that can be freely removed:

    git rm -rf .openshift README.md diy misc

Commit the changes:

    git commit -am "Removed template application source code"

### Step 3: Pull Source code from GitHub

    git remote add upstream https://github.com/pfkenny/fido-rp-server.git
    git pull -s recursive -X theirs upstream master

### Step 4: Add the connection details to your IdentityX server

Follow the steps at http://daoninc.github.io/fido-integration/#adminConsole to create your application configuration on the IdentityX server and to create your wrapping key, cert & token
Once you have completed this task next copy the wrapping keystore and the credential.properties file to src/main/resources folder.
Finally follow the steps at http://daoninc.github.io/fido-integration/#adminConsole section 9.1.2 to edit the src/main/resources/fido_config.properties file and set the connection details to the IdentityX FIDO Server

### Step 5: Push changes

The basic template is ready to be pushed:

	git push

The initial deployment (build and application startup) will take some time (up to several minutes). Subsequent deployments are a bit faster, although starting Spring Boot application may take even more than 2 minutes on small Gear:

	Tomcat started on port(s): 8080/http
	Started Application in 125.511 seconds

You can now browse to: https://fidorpsa-yournamespace.rhcloud.com/manage/health and you should see:

	{
		"status": "UP"
	}

You can then browse to "/facets" to see the facets for the server indicating that it is communicating successfully with IdentityX or /h2 to log into the database.

### Step 6: Adding Jenkins

Using Jenkins has some advantages. One of them is that the build takes place in it's own Gear. To build with Jenkins, OpenShift needs a server and a Jenkins client cartridge attached to the application. Creating Jenkins application:

	rhc app create ci jenkins

And attaching Jenkins client to the application:

	rhc cartridge add jenkins-client --app fidorpsa

You can now browse to: http://ci-<namespace>.rhcloud.com and login with the credentials provided. When you make next changes and push them, the build will be triggered by Jenkins:

	remote: Executing Jenkins build.
	remote:
	remote: You can track your build at https://ci-<namespace>.rhcloud.com/job/boot-build
	remote:
	remote: Waiting for build to schedule.........

And when you observe the build result, the application starts a bit faster on Jenkins:

	Started Application in 52.391 seconds


##Integrating with other FIDO UAF Servers
One question you might have is whether this Relying Party Server can be used to connect to other FIDO UAF servers.  The FIDO Alliance UAF specifications describe the format of the messages to be sent between the FIDO server and the FIDO client but not the method of sending those messages i.e. the FIDO Server API.  This allows different vendors to implement different approaches but it does mean that in order for this Sample RP Server to work with another server, it would need to be changed to call the interface to that server.  The API you will see used by this project to talk to IdentityX is Daon's interface.  Other FIDO UAF Servers are likely to have a different interface.

If you have altered this project to work with another FIDO UAF Server, please send us a pull request and we will see if we can incorporate it in a way which allows a simple configuration flag to determine the target server.

##Made Changes?
If you have improvements or changes you feel would improve this project, please send us a pull request.

##Thanks
Our thanks to the FIDO Alliance who are helping to move the world beyond the tyranny of passwords.

##Help
Contact us via email at support@daon.com. You can also see the IdentityX documentation for more information.

##License
Apache 2.0, see [LICENSE](https://github.com/daoninc/fido-rp-server/blob/master/LICENSE.md).
