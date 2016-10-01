# Lore Expansion

![Logo](https://i.imgur.com/U3dcH9z.png)

[![Build Status](http://tehnut.info/jenkins/buildStatus/icon?job=LoreExpansion/1.10)](http://tehnut.info/jenkins/job/LoreExpansion/1.10) [![](http://cf.way2muchnoise.eu/full_lore-expansion_downloads.svg)](https://minecraft.curseforge.com/projects/lore-expansion) [![](http://cf.way2muchnoise.eu/versions/For%20Minecraft_lore-expansion_all.svg)](https://minecraft.curseforge.com/projects/lore-expansion) ![Badge](https://img.shields.io/badge/Made_with-Memes-red.svg?style=flat-square) [![license](https://img.shields.io/github/license/mashape/apistatus.svg?style=flat-square)](https://github.com/TehNut/LoreExpansion/blob/1.10/LICENSE.md)

Lore Expansion is a mod designed by CyanideX and brought to fruition by dmillerw. It is now maintained by TehNut. It's sole purpose is to allow modpack authors and mapmakers the ability to expand the narrative behind the lore. While it was intended to be used alongside the Hardcore Questing Mode mod, it can just as easily be used by adventure map makers.

The lore is fully customizable through the use of JSON files and allows players to have complete control over how their story progresses. Change everything from the title, body text to page numbers and even dimension specificity. Record your audio, place the .ogg file in the chosen folder and link the filename (without the extension) to the sound variable in the JSON file. Players can also specify the optional dimension specific tags to fit with your theme; leaving out the tag will label the lore as global and can be received by all players.

Using Lore Expansion with HQM is simple. Each page you add will be registered as a new item; simply link the chosen lore page as a quest reward or have the player discover them through other means. Discovered pages are automatically added to the lore book and can be accessed by right clicking the book in hand or pressing the configurable hotkey (default L) to bring up the GUI.

[![Tutorial](https://i.imgur.com/T006Ki8.png)](https://www.youtube.com/watch?v=DBOecyn7TFE)

Click on the image above to view the provided in-game tutorial, narrated by the one and only Vaygrim.

 

The boxes shown on the left will highlight how many pages are able to be found and as pages are collected they will fill their numerical slots. For example, if the player find the first few pages but happens to miss a page in between, there will be an empty box showing you that one of the pages was missed. Lore can be replayed at any time by clicking a page icon on the left and clicking "Play Again". Closing the book will allow you to continue playing while listening to the lore.

If lore is tagged to be available in a specific dimension, they will be filed on different pages. Using the tabs on the left of the book, players can scroll through dimensions to view which pieces of dimension-specific lore has been discovered.

**Narrative Features:**

We have added the ability for certain lore pages to be tagged as primary lore which, when picked up by the player, will automatically play without the need to open your journal. This feature will allow for unique narrative during key points in an adventure map; use command blocks to give the page to players within proximity and the lore will automatically file into the journal and play.

A useful example would be a map that takes place in a space station or ship. At certain points the lore can trigger narrative audio to simulate announcements over a loudspeaker or intercom. Better yet, it could be the voice of a purple female AI inside your helmet directing you to certain checkpoints.

**Command Triggers:**

Lore pages can also be flagged to execute commands upon filing. As the player picks up the lore pages, any listed command will be run silently, similar to how command blocks function. This unique addition avoids the hassle of running redstone clocks to trigger command blocks and can contain more than one function.

**Lore JSON Files Found Here:** `%config%/LoreExpansion/lore/*.json`