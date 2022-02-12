- [CS 1632 - Software Quality Assurance](#cs-1632---software-quality-assurance)
  * [Desciption](#desciption)
  * [Task 1: Write test cases](#task-1-write-test-cases)
    + [Tips for Writing assertions for each Test](#tips-for-writing-assertions-for-each-test)
    + [Other Tips](#other-tips)
  * [Task 2: Add test cases to test suite and save project](#task-2-add-test-cases-to-test-suite-and-save-project)
  * [Task 3: Export test suite to JUnit class](#task-3-export-test-suite-to-junit-class)
    + [Why export to a JUnit class?](#why-export-to-a-junit-class-)
    + [How to export to JUnit for Selenium IDE](#how-to-export-to-junit-for-selenium-ide)
    + [JUnit set up (Chrome browser specific)](#junit-set-up-chrome-browser-specific)
    + [JUnit set up (Firefox browser specific)](#junit-set-up-firefox-browser-specific)
    + [Running the JUnit class](#running-the-junit-class)
  * [Tips for JUnit + Selenium problem solving](#tips-for-junit--selenium-problem-solving)
    + [How to disable pop-ups (Chrome browser specific)](#how-to-disable-pop-ups-chrome-browser-specific)
    + [How to disable pop-ups (Firefox browser specific)](#how-to-disable-pop-ups-firefox-browser-specific)
  * [Submission](#submission)
  * [GradeScope Feedback](#gradescope-feedback)
  * [Groupwork Plan](#groupwork-plan)
  * [Resources](#resources)

# CS 1632 - Software Quality Assurance

* DUE: Oct 15 (Friday), 2021 11:59 PM

**GitHub Classroom Link:** TBD

## Desciption

For this assignment, you and a partner will write a systems-level, automated
black-box tests for the Reddit website using the Selenium IDE.  Specifically,
we are going to test the r/cats subreddit at:

https://www.reddit.com/r/cats/

It was chosen because it is a nice safe subreddit which is policed pretty well.  Besides, it's cats.

First, let's start by adding the Selenium IDE browser extension for your web
browser by selecting "Chrome Download" or "Firefox Download" on the below
website (**Update: the "Chrome Download" option seems to be temporarily
unavailable in the Chrome web store so you will have to install Firefox and
then install the extension there.**):

https://www.selenium.dev/selenium-ide/

Then, open Selenium IDE by clicking on the newly created browser extension with
the "Se" symbol.  You should see a pop up window that looks very similar to the
one shown on the lecture slides.

## Task 1: Write test cases

Write a test case for each requirement listed in
[requirements.md](requirements.md).  Name each test case same as the
requirement name (e.g. FUN-TITLE).  I'm asking you to do this for the purposes
of GradeScope autograding.  Normally, you would use a more descriptive name.

Remember, each test must end with an assertion!  The list of available
assertions and other commands are available at:

https://www.selenium.dev/selenium-ide/docs/en/api/commands

### Tips for Writing assertions for each Test

You will want to use the below commands and assertions to test each of the requirements:

FUN-TITLE: "assert title"

FUN-JOIN-BUTTON-EXISTS - "assert text"

FUN-SIGNUP-LINK - "store attribute" followed by "assert".  You will be storing
the attribute value to a Selenium variable and asserting on the value of that
variable.  Now there is a defect in Selenium IDE with the "store attribute"
command where the target selector button is disabled when it should be enabled.
A work around is to enter a command such as "assert text" or "click" which
allows you to use the target selector, fill in the locator string using it, and
then revert to "store attribute".  As I said, while Selenium web driver is very
mature, Selenium IDE is a work in progress. :)

FUN-SEARCH-SMELLY-CAT - "assert text"

FUN-RULE-3 - "assert text"

FUN-RULES-10-ITEMS - "assert element present" for the 10th item; "assert element not present" for the locator for the 11th item.

**Hint:** If you are really stuck, there is a solution project file [Reddit
Cats Solution.side](Reddit%20Cats%20Solution.side) that you can open from
Selenium IDE.  Take a peek but don't loiter!

### Other Tips

Sometimes your test case will not work as expected.  Here are a few hints on how to debug a problem:

1. Check the **Log window** at the bottom of the Selenium IDE.  It will tell you
   which step failed for what reason (in red).

1. Select the test step that failed in the main test case window, and then
   select the **Reference tab** at the bottom pane of the IDE.  It will display
usage instructions for that command.  Remember always, the first argument goes
to the Target field and the second argument goes to the Value field, regardless
of command.

1. Sometimes the target component of a test step is the problem.  The selector
   button tries to generate a **locator string** as best it can using xpath, css
selector, or id tag.  But it is not fool proof.  The problem is, the web page
may change ever so slightly on the next page load (e.g. due to a new post, or a
new comment) and then the locator will stop working.  You will notice that
there is a small down arrow at the end of the target text box.  If you click on
that arrow, you will see alternative locator strings to the current string.
Select the one that looks specific enough to be able to point to the target but
also general enough to not change between page loads.  You do need to try this
out several times to get a feel of what a good locator string is.  Here is an
in-depth discussion about locators:  

   https://www.selenium.dev/documentation/en/webdriver/locating_elements/#element-selection-strategies
   
1. Sometimes you can use an XPATH position locator string to check that an
   element exists at an expected location ("assert element present") or does
not exist ("assert element not present").  But to do this, you have to select
the XPath position locator string in the drop-down list of optional strings in
the Target field.

## Task 2: Add test cases to test suite and save project

1. Choose "Test Suites" from the left panel drop down menu.

1. There will already be a "Default Suite" there with possibly one or more tests.

1. Right click on "Default Suite", or click on the vertical-3-dot context menu button, and select "Rename" and rename to "RedditCats".

1. Right click on "RedditCats", or click on the vertical-3-dot context menu
   button, and select "Add tests".  Make sure all tests are checked as shown in
the below figure.  Press on the "Select" button.

   <img alt="Test Suite" src=test-suite-selection.png width=700>
   
1. Click on the "Save project" button on the top right corner that looks like a
   floppy disk.  Save to file name "Reddit Cats.side" in the exercise root folder.

## Task 3: Export test suite to JUnit class

### Why export to a JUnit class?

There are many reasons why you would want to export to JUnit.

1. You may have a pre-existing testing framework in JUnit (or Python Pytest, or
   JavaScript Mocha, etc).  And you may want to merge the Selenium IDE testing
script to the language and framework of your choice.

1. Exporting to JUnit really gives you a good sense of what's happening under
   the covers (in terms of the actual calls to the webdriver).  Also, if there
is a test case that is particularly hard to nail down just by using Selenium
IDE, you can touch it up in the form of exported Java code.  

1. Selenium IDE also gives the option to export your JUnit test directly to a
   Selenium Grid which can run the test cases in parallel.  This can allow you
to utilize a server farm to finish your testing very quickly, although we will
not explore this option today.

In the end, once you get familiar with the Selenium API and how locator strings
work, you will prefer coding in Java directly to the APIs.  At that stage,
Selenium IDE will feel more like extra baggage rather than a helping hand.  The
Selenium IDE scripting language is Turing complete, but it is primitive
compared to something like Java and the GUI coding interface, while helpful
initially, will start to feel clunky.  Moreover, the full range of Selenium
APIs are not represented in the scripting language (not to mention a few
defects in the subset that it does implement).

This part of the exercise will help you get more familiar with the Selenium API
and get you started on your journey to be a full Selenium programmer.

### How to export to JUnit for Selenium IDE

Once you are done writing your Selenium test suite, let's try exporting the test
suite in Selenium IDE to a Java JUnit test class.  

Follow these instructions:

1. Right click on "RedditCats", or click on the vertical-3-dot context menu
   button, and select "Export".

1. Select "Java JUnit" in the list of language options and optionally check
   "Include step descriptions as a separate comment" to generate more detailed
comments.  Leave other boxes unchecked.

1. Save the resulting file into "RedditCatsTest.java" to the root of the
   exercise 3 directory.

### JUnit set up (Chrome browser specific)

If you generated your JUnit file using the Chrome browser Selenium IDE
extension, you will have to modify it to do some additional set up.

Please add these line(s) to the beginning of the @Before setUp() method in the
generated RedditCatsTest.java file:

```
System.setProperty("webdriver.chrome.driver", "Chrome/chromedriver-win32.exe");
```

If you are not using Windows, replace chromedriver-win32.exe with the webdriver
compatible with your OS.

Note that the Chrome webdriver only works if you have Chrome version 94
installed on your computer (the most recent version as of today).  If you have
a different version of Chrome, you may have to download the appropriate
webdriver from:

https://chromedriver.chromium.org/downloads

Your Chrome version can be obtained by clicking on the vertical-3-dot menu at
the top right corner of your browser, then Help > About Google Chrome.

### JUnit set up (Firefox browser specific)

If you generated your JUnit file using the Firefox browser Selenium IDE
extension, you will have to modify it as well.

Please add these line(s) to the beginning of the @Before setUp() method:

```
System.setProperty("webdriver.gecko.driver", "Firefox/geckodriver-win64.exe");
System.setProperty("webdriver.firefox.logfile", "/dev/null");
```

Again, if you are not using Windows, replace geckodriver-win64.exe with the
webdriver compatible with your OS.  The second logfile property redirects
verbose log messages emitted by the Firefox browser to a null device,
discarding them.  This is done so that those messages don't get interleaved
with JUnit messages and confuse you.

The Firefox webdriver should work for all recent versions of Firefox.

### Running the JUnit class

You can now run the RedditCatsTest JUnit class using the provided
[TestRunner.java](TestRunner.java) using one of the following scripts:

* If you are running Windows:
   ```
   run.bat
   ```

* If you are running Mac or Linux:
   ```
   run.sh
   ```

* You can also run your Selenium tests on Eclipse using the "Run JUnit"
  feature, after opening the provided Eclipse project.

If things go properly, you will see the browser pop up repeatedly for each test
case, perform the actions, and close.  In the command line, you should see "ALL
TESTS PASSED", which is printed by TestRunner if there are no failures.

## Tips for JUnit + Selenium problem solving

1. Often problems that are not apparent in the Selenium IDE commands become
   apparent in the Java code.  Read the Java code to detect problems.  As I
mentioned, there are even some defects in the translation process!

1. There is a quirk with the Reddit website.  The following two websites are very different websites:
   * https://www.reddit.com/r/cats/  
   * https://www.reddit.com//r/cats/  

   You'd be surprised!  Make sure you are accessing the former and not the latter.

1. One common problem with Selenium is that it takes a long time for certain
   web pages or web elements to load and if Selenium proceeds with testing
immediately after opening a page, the tests will fail.  So Selenium provides
APIs to allow you to wait until an event happens (e.g. the element is
loaded).  All the details about which APIs to use on which situations is in
the page:

   https://www.selenium.dev/documentation/en/webdriver/waits/
   
   For your purposes, an implicit wait setting at the beginning should be enough.  Insert the following line in the @Before setUp() method:

   ```
   driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
   ```

   In order to use that line, you will need to also import this library:

   ```
   import java.util.concurrent.TimeUnit;
   ```

   What that does is: for every step, if the corresponding element is
missing, it inserts an implicit wait of 10 seconds before signaling a
failure.  Selenium IDE internally uses an implicit wait time of 30 seconds
when running a script, but when it exports the script to the JUnit test, it
fails to insert that implicit wait in the @Before setUp() method.  So if you
want one, you need to insert it yourself.

1. Another common problem is that depending on the browser window size,
   certain elements may disappear.  For example, the Reddit site would hide
the "rules" bar on the right hand side if the windows is too narrow.  One
way to solve this is to uniformly set the window size at the @Before setUp()
method so that all your test cases are tested on the same dimensions (and
remove all calls to setSize in your test cases):

   ```
   driver.manage().window().setSize(new Dimension(1200, 800));
   ```
  
1. Yet another common problem is that some websites have pesky pop-up
   windows that prevents the Selenium Web Driver from interacting with the
website, resulting in test failure.  For example, the reddit.com has a pop
up window asking whether you want to "Show notifications" for the website
when visited for the first time.  Until you click "Block" or "Allow" on the
pop up, the rest of the website is inaccessible.  Once you click on a
choice, reddit.com will store your choice in a cookie and not ask you on
subsequent visits.

   When testing with Selenum IDE, the pop-up will not occur because most
likely this is not our first visit and as a browser extension, Selenium IDE
has access to cookies.  However, when testing with the exported JUnit test,
JUnit launches a standalone web browser instance in its own sandbox so it
will not have access to pre-existing cookies.  That means the notification
pop up will occur on the JUnit test every time.

### How to disable pop-ups (Chrome browser specific)

If you do not want any interference from pop-ups during testing, there is a
simple way to do it.  Replace the following line in the @Before setUp() method:

```
driver = new ChromeDriver();
```

with the following set of lines:


```
ChromeOptions options = new ChromeOptions();
options.addArguments("--disable-notifications");
driver = new ChromeDriver(options);
```

In order to use ChromeOptions, you will need to import the class at the top:

```
import org.openqa.selenium.chrome.ChromeOptions;
```

The "Show notifications" pop up is not the only annoying pop up out
there. Most of us are also familiar with the "Know your location" pop up.
To disable this one, simply add this line to the above:

```
options.addArguments("--disable-geolocation");
```

### How to disable pop-ups (Firefox browser specific)

If you do not want pop-ups during testing with Firefox, it.  Replace the
following line in the @Before setUp() method:

```
driver = new FirefoxDriver();
```

with the following set of lines:


```
FirefoxOptions options = new FirefoxOptions();
options.setProfile(new FirefoxProfile());
options.addPreference("dom.webnotifications.enabled", false);
driver = new FirefoxDriver(options);
```

In order to use FirefoxOptions, you will need to import these at the top:

```
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
```

In order to disable the "Know your location" geolocation pop up, simply add
this line to the above changes to FirefoxOptions:

```
options.addPreference("geo.enabled", false);
options.addPreference("geo.provider.use_corelocation", false);
options.addPreference("geo.prompt.testing", false);
options.addPreference("geo.prompt.testing.allow", false);
```

## Submission

Each pairwise group will do one submission to GradeScope as usual.  The
submitting member must use the "View or edit group" link at the top-right
corner of the assignment page after submission to add his/her partner.  

Submit the repository created by GitHub Classroom for your team to GradeScope
at the **Exercise 3 GitHub** link.  Make sure you the files "Reddit Cats.side"
and "RedditCatsTest.java" are in your submission.  Once you submit, GradeScope
will run the autograder to grade you and give feedback.  If you get deductions,
fix your code based on the feedback and resubmit.  Repeat until you don't get
deductions.

My solution test cases are stored as the [Reddit Cats
Solution.side](Reddit%20Cats%20Solution.side) file afore mentioned.  Compare
with your test cases (this is not the only way to implement the test cases).
It is in JSON format so you should be able to open it with a text editor and
trace it with your eyes, if that's what you prefer.

## GradeScope Feedback

The GradeScope autograder works in 2 phases:

1. **RedditCatsTest on https://www.reddit.com/r/cats**: This tests your
   RedditCatsTest.java file on the cats subreddit as originally intended.  All
of your tests should pass.

1. **RedditCatsTest on https://www.reddit.com/r/dogs**: This tests your
   RedditCatsTest.java file on the dogs subreddit, repeating the same steps but
for a different webpage.  Now some tests should pass but some should fail.
Specifically, FUN-JOIN-BUTTON-EXISTS and FUN-SEARCH-SMELLY-CAT should pass and
the rest shold fail.

You may be curious how I was able to run the tests on the GradeScope docker
images when they most likely don't have displays to render the Chrome browser.
The Chrome webdriver, as well as other webdrivers, can be run in "headless"
mode.  That is, the tests can be performed inside the web engine without having
to actually display the page.  This is very common practice since in a work
setting, testers will be running tests on server machines or even on the cloud
in docker images like I did.  If you need to do this in the future, you can
achieve this by passing options when creating the Chrome webdriver:

```
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless");
options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
driver = new ChromeDriver(options);
```

## Groupwork Plan

I suggest that each partner in the group works on this individually.  There is
only one single file that you will be modifying the Selenium IDE (.side)
project file, or the RedditCatsTest.java file.  And it would be difficult for
both of you to work on that single file.  Parallel modifications would result
in frequent merge conflicts.  When both of you are done, compare your work and
submit one finalized version to GradeScope.

## Resources

These links are the same ones posted at the end of the slides:

* Selenium IDE Getting Started:
https://www.selenium.dev/selenium-ide/docs/en/introduction/getting-started

* Selenium IDE Command Reference:  
https://www.selenium.dev/selenium-ide/docs/en/api/commands

* Selenium WebDriver Tutorial:
https://www.selenium.dev/documentation/webdriver/
