# Passwørd Safe

A simple, secure password storage tool which allows you to keep all your passwords in one file, protected with a single passphrase.
Available for Windows, Mac, Linux, Solaris. 

![alt text](https://github.com/andy-goryachev/PasswordSafe/raw/master/screenshots/screenshot.png "Application Screenshot")

### Why?

This application is inspired by Bruce Schneier's <a href="http://passwordsafe.sourceforge.net/">Password Safe</a> program.  

At first, I needed to have a cross-platform tool that I could run on Mac and Linux.  

Then I discovered that a SHA-256 hash of the stretched key in the original Password Safe file is provided in the clear.  I don't know if the opposition has rainbow tables or specialized hardware to brute force a 256 bit hashes.  The documentation explains that it "is used to verify that the user has the correct passphrase", a goal that can be achieved in a more secure way, for instance, by using <a href="http://en.wikipedia.org/wiki/Authenticated_encryption">authenticated encryption</a>.

### License

This project is licensed under the terms of the MIT license.
