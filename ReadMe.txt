pin 'em up

version 0.5

Contents
------------------------
1. About
2. System requirements
3. Changelog
4. Planned features
5. Contact information
6. Thanks



1. About
------------------------
  pin 'em up is a tool for placing small notes/post-its (e.g. TODOs/reminders)
  on your desktop and organizing them in various ways (categorize, hide, upload, 
  export, etc.). Due to platform independence, the notes can also be shared
  on different operating systems.

  Features:
    - OS-independence
    - Integrates into system-tray
    - freely placeable notes
    - many possibilities to customize the note-design
    - (nearly) unlimited user-defined categories
    - fast switch between categories (e.g. "home" and "office")
    - automatically save notes to file
    - open file format (XML)
    - server upload- and download functions (FTP, WebDAV)
    - exporting notes into a text-file



2. System requirements
------------------------
  pin 'em up only runs with Java 6 (JRE) or higher
  
  (get the latest version at http://java.sun.com)



3. Changelog
------------------------
  0.5:
    - added support for webdav(s) servers to up-/download the notesfile
    - added support for different languages
    - added german translation
    - changed "active" symbol in menus from '#' to '->'
    - notes are now sent to background when application starts or new notesfile gets loaded
    - only one instance of settings-dialog and category-dialog allowed
    - BUGFIX: buttons in popup-messages are now displayed in the correct language
    - many internal code improvements


  0.4:
    - BUGFIX: font type was changed to SERIF on change of font-size
    - BUGFIX: notesfile now allows negative note-positions (fixes "notesfile not valid" error)
    - notecolor can be changed (9 different colors so far)
    - dialog for managing categories
    - title of note-windows changed (e.g. displayed when pressing alt+tab)
    - some internal code improvements


  0.3:
    - changed default font to sans-serif
    - added new closeicon (can be chosen in settings-dialog)
    - category can be displayed in title-bar of each note
    - variable number of categories (can be added / removed)
    - categories are now stored in the notesfile and not in the settings anymore
    - new package names
    - new website-URL: pinemup.sourceforge.net
    - licensed under GPLv3 (or later) now
    - update check available
    - redesign of settings dialog
    - size of a note cannot be less than 30x40 pixels anymore (makes sure a note can be resized at anytime)
    - automatically jump into the textarea of a note when it is added
    - notes are now stored in XML format (-> files incompatible to 0.2)
    - file on FTP server now has the same name as the local file
    - user can now decide, if the ftp-password shall be stored on disk or not
    - focus not in note on program start
    - deletion of a note must now be confirmed by default (can be turned off)
    - new trayicon(s)
    - added icon for windows executable
    - scrollbar is now hidden when note does not have focus
    - autosize function works properly now
    - some major internal code improvements


  0.2:
    - double click on tray-icon adds new note
    - fixed activation of "Apply"-Button in Settings-Dialog
    - fixed problem with resizing of note-windows when scrollbar appeared
    - double clicking on top-panel of a note fits it's size to the content (vertically only)
    - some internal code improvements
    - notes can be exported to textfile
    - font size can be adjusted for each note
    - new note-design
    - settings are stored in Users Preferences-Object instead of config-file (old file can be removed)
    - notes-file can be chosen by user
    - always-on-top-function added


  0.1:
    - first release



4. Planned features
------------------------
  - option: assign all visible notes to one category
  - schedule --> popup at a certain time
  - special TODO notes
  - rightclickmenu in textarea (cut, copy, paste, ...)
  - more supported languages
  - more freedom with fonts / notes look & feel
  - support for global shortcuts
  - plugins



5. Contact information
------------------------
  Author: Mario Ködding
  Email: mario@koedding.net
  ICQ: 77590604

  Project-Homepage:
  http://pinemup.sourceforge.net



6. Thanks
------------------------
  Special Thanks go to:
  - Jan Laczay for his contributions to the project website and the note-color design
  - Andreas Kwiatkowski for redisigning the closeicon
  - Michael Mrose for his contributions to the project website and for testing the new version
  - Natalie Nöring for testing the new version
