 Merc - The Freelancers Eclipse Plug-in
========================================

Description
-----------
Merc is a a bundle of Eclipse plug-ins, intended to help the freelancers day to day activities 
*(NOTE:although the application consists of several plug-ins, in the remaining part of the document it will be refered
to as "plug-in" or "application")*. 
The goal of the application is to permit the user to browse freelancing offers, bid on projects and communicate with buyers across
several freelancing websites.

Because the plug-in is still in its early stages, the only freelancing website supported for the time being is 
the sandbox environment of [Freelancer.com](http://sandbox.freelancer.com). 
 	
System Requirements
--------------------	
- Eclipse 4.0
- Java 1.6

Installation
------------
Download the jars from the [release](https://github.com/albert-toke/Merc/releases/tag/v0.1-alpha) page and copy them in the `dropins` folder, in your Eclipse directory.

Configuration
-------------
- Create an account on the freelancing website [sandbox.freelancer.com](http://sandbox.freelancer.com)
- Start Eclipse.
- Open the **Preferences** Page(*Window -> Preferences -> Merc Preferences*). 
- Authorize the application:
	- Select the freelancing provider **Freelancer** in the combo labeled **Provider**.
	- Click on the button **Request Authorization Code**.
	- A browser will appear below the button where you must login with your user and password and grant access
	  to the application.
	- After granting access an authorization code will be displayed on the website, copy this code into field 
	  labeled **Authorization code**.
	- Press the button **Request Access Token**.
	- Press the button **OK**.
- Open the Merc view (*Window -> Show View -> Other... -> Merc Category -> Merc View*)
- Start Using Merc.
	
Operation Instructions
----------------------
*soon...*

Plug-in manifest
----------------
The Merc application consists of the following plug-ins:

- `Merc.ExternalJars` - Contains the APIs [Scribe](https://github.com/fernandezpablo85/scribe-java) and [Jackson](http://jackson.codehaus.org), which are used 
  by the business of the application.
- `Merc.Plugin` - Contains the business of the application.
- `Merc.Freelancer.Plugin` - Represents the plug-in responsible for the communication with the freelancing website [Freelancer](sandbox.freelancer.com) through its own API.
- `Merc.GUI` - Contains the GUI and some parts of the business logic.

License
-------
### Merc
GPL 3 License

Copyright (C) 2013  Albert Toke

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.    

### Scribe
The MIT License

Copyright (c) 2010 Pablo Fernandez

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

### Jackson
[Apache License(AL)2.0](http://www.apache.org/licenses/LICENSE-2.0)

Known bugs/issues
----------
- Paging is not implemented, hence only the first 50 search results are shown.
- No error message when the plug-in can not access the internet.
- No possibility for configuring proxy settings.
- Result table containing unnecessary columns.