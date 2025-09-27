## 🎉 Alley v2.2.8

### 🔧 **PATCH RELEASE** - Bug Fixes & Improvements

**Previous Version:** `2.2.7`
**New Version:** `2.2.8`

### 📝 Changes:

- feat: finalize configurable messages by refactoring locale getters and making remaining key messages configurable (edae5483)
- feat: configurable clickable messages (and some other messages... eventually all to be completed within the next final commit to this branch) (55169ada)
- feat: ffa death messages now configurable (and some more) (6d094a7a)
- feat: certain permissions are now customizable and updated config files (423a1f72)
- feat: implemented method to easily retrieve and format command usages, fix the tab-list constantly being disabled, make plenty of more messages configurable, the chat format is now customizable (can also be enabled or disabled depending on the users need). (b5da6fde)
- feat: more configurable messages and consistent placeholders (d7d4163b)
- feat: new config structure/layout, replaced all message locales with two generic implementations (6d6d9489)
- feat: updated config files. Titles, command messages, in game messages, match result messages, etc... now configurable. (d4d4637a)
- chore(locale-service-impl): translate default value color codes (61db0342)
- feat: recode locale system with safe defaults, automatically set missing entries and return defaults if still absent, make additional messages configurable, and add missing usage and description to CommandData annotations as the usage format will eventually be configurable and required for all commands (with a method to retrieve the usage message for convenience) (18512746)
- feat: with all my patience, making some more messages configurable 2.0 (d90aee6b)
- feat: with all my patience, making some more messages configurable... (45a25409)
- feat: fixed ffa add, kick and setup command, same goes for division set wins command (cd75214f)
- chore: what in the world is this command ??? (2cea32ee)
- feat: made more commands and messages configurable (ffa / duel) (2f1a6128)
- feat: division and cosmetic commands are now configurable (433dea5f)
- feat: started making messages configurable (c50fc64e)

---
**Download:** [Alley-2.2.8.jar](https://github.com/RevereInc/alley-practice/releases/download/v2.2.8/Alley-2.2.8.jar)

**Installation:** Place the JAR file in your `plugins/` folder and restart your server.

---
### 🏷️ Version Bump Keywords

- Add `MAJOR` to commit messages for breaking changes (X.0.0)

- Add `MINOR` to commit messages for new features (X.Y.0)

- Add `PATCH` to commit messages for bug fixes (X.Y.Z)

- No keyword = automatic patch bump

- Add `SKIP-RELEASE` or `[skip release]` to skip creating a release

