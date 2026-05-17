# Grama-Khata (Micro-Finance Ledger)

Grama-Khata is a professional offline-first micro-finance ledger application designed for village shopkeepers and small business owners. It replaces traditional handwritten credit books with a secure, digital solution to manage customer credit (Give) and payment (Take) records.

## 🚀 Features

- **Splash & Onboarding**: Professional entry and one-time shop name setup.
- **Customer Management**: Add, Edit, and Delete customers with profile photos using `ActivityResultContracts.PickVisualMedia`.
- **Transaction Management**: 
    - **Give (Credit)**: Record credit given to customers.
    - **Take (Payment)**: Record payments received.
    - **Real-time Balance**: Automatic net balance calculation for every customer.
- **Due Dashboard**: Quickly identify customers with pending dues, sorted by highest amount.
- **Daily Collection Report**: Generate and share a summary of today's total collections.
- **Reminders**: Integrated WhatsApp and SMS reminders for pending dues.
- **Search & Filter**: Real-time customer search by name or phone number.
- **Offline First**: Fully functional without internet using Room Database.

## 🛠 Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Database**: Room (SQLite)
- **UI**: Material Design 3, ViewBinding, Navigation Component
- **Asynchronous Tasks**: Kotlin Coroutines
- **Image Loading**: Glide

## 📸 Project Structure

- `data/`: Contains Room Entity classes, DAOs, Database configuration, and Repository.
- `ui/`: 
    - `fragment/`: UI logic for each screen.
    - `viewmodel/`: Shared ViewModel for business logic and data state.
    - `adapter/`: RecyclerView adapters using DiffUtil for performance.
- `util/`: Helper classes like Room Converters.

## 🏁 How to Run

1. Clone the repository or download the source code.
2. Open the project in **Android Studio (Ladybug or higher)**.
3. Connect an Android device or Emulator (API 26+).
4. Click **Run** (Shift + F10).

## 📄 License

This project is developed as part of an internship.

---

### **Mentor Verification Guide**

This project demonstrates proficiency in:
- Room Persistent Library
- Navigation Component with SafeArgs
- MVVM Architecture & Repository Pattern
- Material Design & Responsive Layouts
- Intent-based external interactions (WhatsApp/SMS/Sharing)
