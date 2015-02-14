﻿# Passwørd Safe

A simple, secure password storage tool which allows you to keep all your passwords in one encrypted file.
For Windows, Mac, Linux, Solaris. 

![alt text](https://github.com/andy-goryachev/PasswordSafe/raw/master/screenshots/screenshot.png "Application Screenshot")

### Why?

This application is inspired by Bruce Schneier's <a href="http://passwordsafe.sourceforge.net/">Password Safe</a> program.  

At first, I needed to have a cross-platform tool that I could run on Mac and Linux.  

Then I discovered that a SHA-256 hash of the stretched key in the original Password Safe file is provided in the clear.  The documentation explains that it "is used to verify that the user has the correct passphrase".  I am not so sure.  I don't know if the opposition has rainbow tables or specialized hardware to brute force a 256 bit hashes, but it feels like this can be a backdoor.  In any case, this goal that can be easily achieved in a more secure way by using, for instance, <a href="http://en.wikipedia.org/wiki/Authenticated_encryption">authenticated encryption</a>.

### License

This project is licensed under the terms of the MIT license.

### Official Site

<a href='http://goryachev.com/products/password-safe/index.html'>goryachev.com</a>
<img src='http://goryachev.com/EVIL-TRACKER.png' border=0 width=1 height=1>
