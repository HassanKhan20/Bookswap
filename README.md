# BookSwap — Textbook Marketplace App

CSE 3310 Spring 2026 · Team 1 · Anas Alhssan, Mustafa Nazeer, Hassan Khan

Android app (Kotlin + Room/SQLite) that lets students buy and sell used textbooks.
Matches the blue + orange theme used in the presentation.

## Running the demo (for the in-class presentation)

1. Open **Android Studio**.
2. **File → Open…** and select this folder (`BookSwap`).
3. Wait for Gradle sync to finish (first time downloads dependencies).
4. Start an emulator from **Device Manager** (e.g. Pixel 6, API 34). Any API ≥ 26 works.
5. Click **Run ▶** (or `Shift+F10`). The app launches on the emulator window — no browser involved.

The emulator window is just a panel on the laptop, so you can Alt-Tab between the slides and the live app during the presentation.

## Demo script (mirrors the "Demo" slide)

1. On the Login screen, tap **Create an account** → register with any name/email/password.
   (Or log in with the seeded seller: `seller@bookswap.app` / `demo1234`.)
2. Home screen shows 8 pre-seeded textbooks. Try the search bar with "math", "cse", or an author name.
3. Tap any textbook → details screen → **Add to Cart**.
4. Tap **Cart** in the header → review items and the running total.
5. Tap **Checkout** → fill in mock card info (e.g. `4242 4242 4242 4242`, `12/28`, `123`) → **Pay (Mock)** → confirmation screen.

## Tech stack

- Kotlin 1.9 · Android Gradle Plugin 8.2 · compileSdk 34, minSdk 26
- Room 2.6 (SQLite) — local persistence for users, textbooks, cart, orders
- Material Components + View Binding for UI
- KSP for Room compiler

## Project layout

```
app/src/main/
├── AndroidManifest.xml
├── java/com/team1/bookswap/
│   ├── BookSwapApp.kt          # Application — seeds demo data on first launch
│   ├── Session.kt              # In-memory current-user holder
│   ├── LoginActivity.kt        # Login screen
│   ├── RegisterActivity.kt     # Sign-up screen
│   ├── HomeActivity.kt         # Browse + search grid
│   ├── DetailsActivity.kt      # Textbook details + add to cart
│   ├── CartActivity.kt         # Cart list + total
│   ├── CheckoutActivity.kt     # Mock payment + confirmation
│   ├── adapters/               # RecyclerView adapters
│   └── db/                     # Room entities, DAOs, database
└── res/
    ├── layout/                 # XML screens + row layouts
    ├── drawable/               # Shape drawables (header, cards, book cover)
    └── values/                 # colors, strings, themes
```

## Theme colors

- Primary blue: `#1E3A8A` (navy)
- Accent orange: `#F97316`
- Light blue: `#3B82F6`
- Soft background: `#F1F5F9`

## Reset demo data

The seed runs once (when the textbook table is empty). To re-seed, in Android Studio:

- **Device Manager → Wipe Data** on the emulator, or
- **Settings → Apps → BookSwap → Storage → Clear storage** on the device.
