# Passwørd Safe

A simple, secure password storage tool which allows you to keep all your passwords in one file, protected with a single passphrase.
Available for Windows, Mac, Linux, Solaris. 

![alt text](https://github.com/andy-goryachev/PasswordSafe/screenshots/screenshot.png "Application Screenshot")

## Why?

This application is inspired by Bruce Schneier's http://passwordsafe.sourceforge.net/ Password Safe program.  
At first, I needed to have a cross-platform tool that I could run on Mac and Linux.  
Then I discovered that a SHA-256 hash of the stretched key in the original Password Safe file is provided in the clear.
