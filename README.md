[README.md](https://github.com/user-attachments/files/34242781/README.md)
# User Local Auth – Email Verification

A Spring Boot REST API that handles **local user registration with email verification**. A new user signs up, receives an email with a verification link, and their account is activated once they click it.

## About the Project

Most applications that have user accounts need to make sure the email address a person signs up with really belongs to them. Without that check, anyone can register with a fake or someone else's address, which leads to spam accounts, abuse, and no reliable way to contact users or recover accounts.

This project is a backend service that solves that problem. It covers the **sign-up half of an authentication system**:

1. A person registers with a username, email and password.
2. The service stores the account as **inactive** and emails a unique link to the address provided.
3. Only when the person clicks that link does the account become **active**.

It's built as a standalone, reusable piece that can be plugged into a larger application (a web shop, a social app, an internal tool) as its registration module. It's also a learning project that demonstrates how Spring Security, JPA, a relational database and a third-party email API fit together in a real-world flow.

### The idea behind the verification flow

- **Inactive until proven:** a new account can't be treated as valid until the email is confirmed.
- **Token = proof of mailbox access:** the link contains a random, hard-to-guess token (UUID) stored in the database and tied to one user. Only someone who can read that mailbox can use it.
- **Single use:** once a token is used, it's marked inactive, so the same link can't be replayed.
- **No plain-text passwords:** passwords are hashed before they reach the database.

### Data model

| Table | Purpose | Key fields |
|---|---|---|
| `users` | Registered accounts | id, username, email, hashed password, status (`INACTIVE` / `ACTIVE`) |
| `user_token` | Verification tokens | id, userId, token, status (`ACTIVE` / `INACTIVE`) |

### Current scope

This version handles **registration and email verification only**. Logging in, sessions or JWT, password reset and role management are not implemented yet

## Features

- User registration with username, email and password
- Passwords hashed before storing (never saved in plain text)
- Random, single-use verification tokens (UUID)
- Verification email sent through [Mailtrap](https://mailtrap.io)
- Accounts stay `INACTIVE` until the email is verified
- Secrets kept out of the code with a `.env` file

## Tech Stack

| Area | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.1.2 |
| Security | Spring Security |
| Database | PostgreSQL, Spring Data JPA |
| Email | Mailtrap Java SDK |
| Build | Maven |

## How It Works

1. Client sends `POST /api/auth/register` with the user's details.
2. The password is hashed and the user is saved with status `INACTIVE`.
3. A random token is generated and stored for that user.
4. A verification email containing a link with the token is sent.
5. The user opens `/api/auth/verify?token=...`.
6. The token is marked as used and the user becomes `ACTIVE`.

## Project Structure

```
src/main/java/com/Ash/user_local_auth_email_verification
├── config       # SecurityConfig (public / protected paths)
├── controller   # Registration and verification endpoints
├── dto          # UserDTO (request body)
├── entity       # User, UserToken, Status
├── db           # JPA repositories
└── service      # Registration, token, email and verification logic
```

## Getting Started

### Prerequisites

- JDK 25
- Maven (or the included `./mvnw`)
- PostgreSQL
- A free [Mailtrap](https://mailtrap.io) account

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd YOUR_REPO
```

### 2. Create the database

```sql
CREATE DATABASE email_verification_demo;
```

### 3. Configure environment variables

Copy the example file and fill in your own values:

```bash
cp .env.example .env
```

```
MAILTRAP_API_TOKEN=your_mailtrap_token_here
DB_PASSWORD=your_db_password_here
```

> `.env` is listed in `.gitignore`. Never commit it.

### 4. Get a Mailtrap token

1. In Mailtrap, go to **Settings → API Tokens → Add Token**.
2. Give the token **Admin** access to your sending domain.
3. Copy the token into `.env`.

> **Note:** Mailtrap's demo domain (`demomailtrap.co`) can only send emails to your own Mailtrap account email. To send to any address, verify your own domain in Mailtrap and change the `from` address in `EmailService`.

### 5. Run the app

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

## API Endpoints

### Register

```
POST /api/auth/register
Content-Type: application/json
```

```json
{
  "username": "johndoe",
  "password": "your_password",
  "email": "john@example.com"
}
```

Response: `200 OK` – `user created successfully`

### Verify email

```
GET /api/auth/verify?token=<token_from_email>
```

Response: `200 OK` – `token verified`, or `400 Bad Request` for an unknown or invalid token.

## Testing with Postman

1. Create a **POST** request to `http://localhost:8080/api/auth/register`.
2. Under **Body**, choose **raw → JSON** and paste the example above.
3. Send the request and check your Mailtrap-linked inbox for the verification email.
4. Click the link, or call the verify endpoint with the token.
5. Check the database: `SELECT username, email, status FROM users;`

## Roadmap

- [ ] Enforce the 24-hour token expiry
- [ ] Reject duplicate emails and usernames
- [ ] Input validation (email format, password strength)
- [ ] "Resend verification email" endpoint
- [ ] Login endpoint with JWT
- [ ] Send emails asynchronously
- [ ] Configurable base URL for the verification link (instead of `localhost`)
- [ ] Unit and integration tests

## Security Notes

- Passwords are hashed with Spring Security's `PasswordEncoder`.
- Secrets (database password, Mailtrap token) are loaded from environment variables.
- Verification tokens become unusable after the first successful verification.

## License

This project is open source and available under the [MIT License](LICENSE).
