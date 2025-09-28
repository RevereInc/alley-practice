## 🎉 Alley v2.2.9

### 🔧 **PATCH RELEASE** - Bug Fixes & Improvements

**Previous Version:** `2.2.8`
**New Version:** `2.2.9`

### 📝 Changes:

- chore: passed the player's name as a constructor argument to Profile during login. The previous logic relying on Bukkit.getOfflinePlayer(uuid).getName() could return null for players joining for the very first time, causing a validation error when the new profile was immediately saved to the database. (014ced4c)

---
**Download:** [Alley-2.2.9.jar](https://github.com/RevereInc/alley-practice/releases/download/v2.2.9/Alley-2.2.9.jar)

**Installation:** Place the JAR file in your `plugins/` folder and restart your server.

---
### 🏷️ Version Bump Keywords

- Add `MAJOR` to commit messages for breaking changes (X.0.0)

- Add `MINOR` to commit messages for new features (X.Y.0)

- Add `PATCH` to commit messages for bug fixes (X.Y.Z)

- No keyword = automatic patch bump

- Add `SKIP-RELEASE` or `[skip release]` to skip creating a release

