import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Register() {
  // useState stores what the user types in the form
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // errors from backend validation
  const [errors, setErrors] = useState({});

  // success message after registration
  const [success, setSuccess] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault(); // stop page from reloading on form submit
    setErrors({});
    setSuccess("");

    try {
      // send POST request to Spring Boot
      await axios.post("http://localhost:8080/api/auth/register", {
        username,
        password,
      });

      setSuccess("Registration successful! Redirecting to login...");
      setTimeout(() => navigate("/login"), 2000);

    } catch (error) {
      if (error.response) {
        // backend returned an error response
        setErrors(error.response.data);
      }
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Create Account</h2>

        {/* show success message */}
        {success && <p style={styles.success}>{success}</p>}

        <form onSubmit={handleSubmit}>
          <div style={styles.field}>
            <label style={styles.label}>Username</label>
            <input
              style={styles.input}
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter username"
            />
            {/* show backend validation error for username */}
            {errors.username && <p style={styles.error}>{errors.username}</p>}
            {errors.error && <p style={styles.error}>{errors.error}</p>}
          </div>

          <div style={styles.field}>
            <label style={styles.label}>Password</label>
            <input
              style={styles.input}
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
            />
            {/* show backend validation error for password */}
            {errors.password && <p style={styles.error}>{errors.password}</p>}
          </div>

          <button style={styles.button} type="submit">
            Register
          </button>
        </form>

        <p style={styles.link}>
          Already have an account?{" "}
          <span
            style={styles.linkText}
            onClick={() => navigate("/login")}
          >
            Login
          </span>
        </p>
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
    backgroundColor: "#f0f2f5",
  },
  card: {
    backgroundColor: "white",
    padding: "40px",
    borderRadius: "12px",
    boxShadow: "0 4px 20px rgba(0,0,0,0.1)",
    width: "100%",
    maxWidth: "400px",
  },
  title: {
    textAlign: "center",
    marginBottom: "24px",
    color: "#1a1a2e",
  },
  field: {
    marginBottom: "16px",
  },
  label: {
    display: "block",
    marginBottom: "6px",
    fontWeight: "600",
    color: "#333",
  },
  input: {
    width: "100%",
    padding: "10px 14px",
    borderRadius: "8px",
    border: "1px solid #ddd",
    fontSize: "14px",
    boxSizing: "border-box",
  },
  button: {
    width: "100%",
    padding: "12px",
    backgroundColor: "#4f46e5",
    color: "white",
    border: "none",
    borderRadius: "8px",
    fontSize: "16px",
    cursor: "pointer",
    marginTop: "8px",
  },
  error: {
    color: "#e53e3e",
    fontSize: "12px",
    marginTop: "4px",
  },
  success: {
    color: "#38a169",
    backgroundColor: "#f0fff4",
    padding: "10px",
    borderRadius: "8px",
    marginBottom: "16px",
    textAlign: "center",
  },
  link: {
    textAlign: "center",
    marginTop: "16px",
    color: "#666",
  },
  linkText: {
    color: "#4f46e5",
    cursor: "pointer",
    fontWeight: "600",
  },
};

export default Register;