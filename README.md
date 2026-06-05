# Zus Assessment — Android Developer Take-Home

A small F&B-style ordering slice: browse a categorised menu, manage a cart, and place a simulated order. Built with Kotlin, Jetpack Compose, and TheMealDB.

## Setup

**Requirements**
- Android Studio (Ladybug or newer recommended)
- JDK 17+ (Android Studio’s bundled JBR works)
- Android SDK with API 26+ emulator or device
- Internet connection (menu data is fetched live)

**Run the app**
1. Clone the repo and open the project root in Android Studio.
2. Let Gradle sync finish.
3. Run the `app` configuration on an emulator or device (minSdk 26).

**Build from the command line (Windows)**

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/`.

No API keys or `local.properties` secrets are required beyond the standard Android SDK path that Android Studio creates locally.

## Data source

**TheMealDB** — free, no authentication.

| Endpoint | Purpose |
|----------|---------|
| `GET https://www.themealdb.com/api/json/v1/1/categories.php` | Menu categories |
| `GET https://www.themealdb.com/api/json/v1/1/filter.php?c={category}` | Meals in a category |

TheMealDB does not provide price or stock status. Both are simulated client-side in `MenuMapper`:
- **Price** — deterministic value derived from meal id (RM 6.90–26.89).
- **Availability** — ~11% of items marked sold out (`id % 9 == 0`).

**Checkout** is simulated in `CartViewModel`: a 1.5s delay, then success (cart cleared) or failure when the cart holds a multiple of 3 line items.

If TheMealDB is unreachable, the menu screen shows an error with a retry action. For a longer-term fix, responses could be stubbed locally via `MockWebServer` or a JSON asset.

## Architecture (brief)

```
data/remote     MealApiService + Moshi DTOs
data/mapper     MenuMapper (domain mapping + simulated fields)
data/repository MenuRepository, CartRepository (@Singleton, Hilt)
domain/model    MenuCategory, MenuItem, CartLine
ui/menu         MenuScreen, MenuViewModel, components
ui/cart         CartScreen, CartViewModel, components
ui/navigation   Navigation 3 — MenuRoute / CartRoute, ZusNavHost
di              NetworkModule (Retrofit + Moshi)
```

- **UI state:** single `MenuUiState` / `CartUiState` per screen, exposed as `StateFlow`, collected with `collectAsStateWithLifecycle`.
- **DI:** Hilt (`@HiltViewModel`, `@Singleton` repositories).
- **Navigation:** Navigation 3 (`NavDisplay`, typed `NavKey` routes, default Compose transitions).
- **Images:** Coil 3.

## Screens

**Menu** — category sidebar with long-press tooltips (Material3 `TooltipBox`) that show the full category description from TheMealDB (`strCategoryDescription`); availability filter chips (ALL / AVAILABLE / SOLD OUT); meal cards with inline cart quantity controls. Sold-out items are dimmed and cannot be added. Loading spinner and error + retry.

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/5e94a0b0-270b-4264-95f1-4d63f03a9736" />

**Cart** — line items with +/- controls, running total, Place Order with loading / success / error states. Empty cart and order-success use the same centred layout. Cart quantity survives rotation.

<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/e7424ec1-a64c-4f47-9825-46798d7c728d" />
<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/417ec0e3-dad8-4c30-a1ce-48141665f50d" />
<img width="1080" height="2412" alt="image" src="https://github.com/user-attachments/assets/898be7b8-ab88-43b5-8b00-5097ca96e373" />

---

## Design questions

### 1. State management

`MenuUiState` holds everything the menu composables need in one immutable data class: categories, the selected category id, items for that category, a `cartQuantities` map keyed by item id, filter, and loading/error flags. Cart quantities are not duplicated inside each `MenuItem`; the ViewModel merges repository cart snapshots into that map so the list and header stay in sync from a single source. Loading is scoped to category fetches (initial load and category switches), while cart updates flow in separately from `CartRepository.snapshot`. That split keeps network state and cart state independent without nested sealed classes or multiple flows in the UI layer.

### 2. API contract

I would push for **authoritative price and availability on every menu item** in the list response. TheMealDB gives rich descriptive content but no commerce fields, which forced client-side simulation and means the app cannot reflect real stock or pricing changes. A production contract should return `price`, `currency`, and `isAvailable` (or `stockStatus`) per item, ideally with a version or `updatedAt` timestamp so the client can detect stale menu data without refetching the full catalogue.

### 3. Configuration change handling

Cart contents persist across rotation via a `@Singleton` `CartRepository` holding a `StateFlow` snapshot, shared by both ViewModels. Navigation 3 entry decorators (`rememberViewModelStoreNavEntryDecorator`, `rememberSaveableStateHolderNavEntryDecorator`) keep each screen’s ViewModel and navigation back stack across configuration changes. **Corner cut:** cart and checkout status are in-memory only — a process death (low-memory kill) loses the cart. `SavedStateHandle` or local persistence was out of scope for the time box but would be the next step for real users.

### 4. What you'd do differently with more time

I would add **ViewModel unit tests** for `MenuViewModel` and `CartViewModel` — specifically category selection after a failed load, cart quantity mapping, checkout loading guard, and the simulated failure rule (`lines.size % 3 == 0`). The logic is small but easy to regress, and tests would document the intended behaviour better than comments. I would also show **price on the meal card** (it is modelled and used in the cart but not yet rendered on Screen 1).

### 5. Production gap

The most significant gap is **no durable cart or session persistence** combined with **simulated commerce data**. A process kill wipes the cart; prices and stock are fabricated; checkout does not hit a real backend or handle idempotency, payments, or order confirmation. Before shipping to real users I would want server-owned menu/pricing/stock, a real checkout API with proper error codes, and local cart backup — plus basic observability (crash reporting, network error metrics) that this slice deliberately omits.

---

## Testing note

No automated tests are included (per assessment scope). With more time, priority tests would be: repository mapping (`MenuMapper` price/availability rules), `CartRepository` add/decrement/clear, and ViewModel state transitions for load/error/checkout paths.
