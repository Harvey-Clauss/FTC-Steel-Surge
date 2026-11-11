# ⚙️ FTC Steel Surge

**FIRST Tech Challenge | Robot Control Software | Java**

**Version: 0.1.1 — pre-release (active development)**

Welcome to the official GitHub repository for **FTC Steel Surge**, a robotics team participating in the FIRST Tech Challenge (FTC).

This repository contains the Java-based software used to control our competition robot, including drivetrain movement, gamepad input, game-piece handling mechanisms, and autonomous routines.

## 🤖 About the Project

Steel Surge is focused on designing, building, programming, and improving a competitive FTC robot.

Our software uses the **FTC SDK** to communicate with REV Robotics hardware, enabling driver-controlled movement and automated robot operations.

### Key Features

- **Mecanum Drivetrain** — Omnidirectional driving, strafing, and rotation.
- **Dual-Controller Support** — Separate gamepads for driving and robot mechanisms.
- **Adjustable Driving Speed** — Configurable drive power and precision controls.
- **Intake and Outtake Systems** — Controls for collecting, transferring, and releasing game pieces.
- **Autonomous Development** — Time-based movement sequences for autonomous operation.
- **Telemetry** — Live hardware status and motor power reporting.
- **Motor Safety** — Motor braking and shutdown behavior.

## 🛠️ Hardware

Our robot uses components from the REV Robotics ecosystem.

| Component | Purpose |
|---|---|
| REV Control Hub | Primary robot controller |
| REV Expansion Hub | Additional motor and servo connections |
| Four Drive Motors | Mecanum drivetrain |
| Intake Motor | Game-piece collection |
| Front Midtake Motor | Game-piece transfer |
| Back Midtake CR Servo | Additional transfer mechanism |
| Two Outtake Motors | Game-piece release |
| Two Gamepads | Driver and operator controls |

Hardware configuration names must match those defined in the source code.

## 🎮 Driver Controls

The robot supports two controllers, allowing the driver to focus on navigation while the operator manages the intake and outtake mechanisms.

### Gamepad 1 — Drivetrain

| Input | Function |
|---|---|
| Left Stick Y | Forward / Backward |
| Left Stick X | Strafe Left / Right |
| Right Stick X | Rotate |
| D-Pad | Individual low-speed wheel power overrides |

The drivetrain uses mecanum wheel power calculations to combine translation and rotation.

The current code uses an **80% drive-power multiplier**, a **20% D-pad wheel-power setting**, and a **20% rotation deadzone**.

### Gamepad 2 — Mechanisms

| Input | Function |
|---|---|
| Right Trigger | Run intake forward |
| Right Bumper | Reverse intake |
| Left Stick Y | Control front midtake |
| Left Stick X | Control back midtake |
| Left Trigger | Activate outtake |

The midtake controls use a configurable deadzone to prevent accidental activation.

### Mechanism Power Settings

| Mechanism | Configured Power |
|---|---|
| Intake Forward | 80% |
| Intake Reverse | 40% |
| Front Midtake | 80% |
| Back Midtake | 80% |
| Outtake | 45% |

These settings can be adjusted in `FTC_TeleOp.java`.

## 📁 Repository Structure

```text
FTC-Steel-Surge/
├── README.md
├── LICENSE
├── src/
│   ├── FTC_TeleOp.java
│   ├── FTC_Autonomous.java
│   └── helpers/
│       ├── DriveTrain.java
│       └── IMU_Controller.java
└── lib/
    └── GoBilda/
        └── info.txt
```

### Source Files

**FTC_TeleOp.java**

Main driver-controlled program. Processes gamepad inputs, controls mechanisms, and displays robot telemetry.

Registered OpMode: `Main - Controller`

**FTC_Autonomous.java**

Autonomous program containing a time-based drivetrain movement sequence.

Registered OpMode: `Main - Autonomous`

**DriveTrain.java**

Helper class responsible for drivetrain motor configuration, movement power, and braking behavior.

**IMU_Controller.java**

Reserved for future IMU-related functionality. Currently empty.

**lib/GoBilda/**

Planned storage for goBILDA CAD resources and mechanical component references. The library is currently incomplete.

## ⚙️ Installation and Setup

### Requirements

- Java Development Kit compatible with your FTC SDK version
- Android Studio
- Official FIRST Tech Challenge SDK
- REV Control Hub and optional Expansion Hub
- Compatible gamepads
- USB or network connection to the robot

### Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/Nickelpacker/FTC-Steel-Surge.git
   ```

2. Download or open an existing FTC Robot Controller SDK project.

3. Copy the Java source files into the FTC project's TeamCode source package.

4. Maintain the correct Java package structure and imports.

5. Configure the robot hardware through the REV Driver Hub or Robot Controller configuration interface.

6. Build and deploy the project using Android Studio.

**Important:** This repository is a source-code repository, not a complete standalone FTC SDK or Android Studio project. Some source files require corrections before successful compilation and deployment.

### GitHub Codespaces

The repository includes a development-container configuration with Java 17,
Android SDK 30, Gradle, and FTC SDK 12.0 compile-time classes.

1. Open the repository on GitHub.
2. Select **Code**, then **Codespaces**.
3. Create a codespace on the branch you want to edit.
4. Wait for the initial `./gradlew compileDebugJavaWithJavac` build to finish.

FTC imports and Java autocomplete should become available after Gradle finishes
syncing. Run the same check again at any time with:

```bash
./gradlew compileDebugJavaWithJavac
```

The Codespace validates and edits TeamCode, but deploying to a Control Hub still
requires the complete FTC Robot Controller project and Android Studio or the
usual FTC deployment workflow.

## 🔧 Hardware Configuration

The TeleOp source references a robot configuration named:

`Steel Surge - Main`

### Required Device Names

| Device Name | Device Type |
|---|---|
| `frontLeft` | DcMotor |
| `frontRight` | DcMotor |
| `backLeft` | DcMotor |
| `backRight` | DcMotor |
| `intake` | DcMotor |
| `midtake_1` | DcMotor |
| `midtake_2` | CRServo |
| `outakeLeft` | DcMotor |
| `outakeRight` | DcMotor |

Device names are case-sensitive.

> Note: The existing source uses the spelling `outake` rather than `outtake`. Keep the hardware configuration consistent with the code unless both are updated.

## 🚗 Drivetrain

Steel Surge uses a four-motor mecanum drivetrain.

The drivetrain supports:

- Forward and backward movement
- Left and right strafing
- Clockwise and counterclockwise rotation
- Combined translation and rotation
- Configurable motor power

The drivetrain helper configures motors to run without encoders and uses BRAKE zero-power behavior.

## 🧭 Autonomous Mode

The autonomous program is under development.

The current source defines an example sequence consisting of:

1. Forward movement
2. Right strafe
3. Left strafe
4. Backward movement

These are time-based commands rather than encoder- or localization-based movements.

**Development status:** The autonomous drivetrain helper is not yet complete. The current implementation should not be considered competition-ready until its compilation and movement behavior have been verified.

## 📊 Telemetry and Diagnostics

The driver-controlled program provides telemetry for:

- Controller availability
- Drivetrain initialization
- Intake and transfer device availability
- Outtake device availability
- Gamepad movement inputs
- Mechanism power levels

Telemetry can help identify hardware configuration problems and troubleshoot robot behavior during testing.

## 🚧 Development Roadmap

Potential future improvements include:

- [ ] Resolve outstanding drivetrain compilation issues
- [ ] Validate all hardware mappings
- [ ] Complete autonomous movement helpers
- [ ] Implement IMU-based heading control
- [ ] Introduce encoder-based movement
- [ ] Improve drivetrain precision controls
- [ ] Add autonomous path planning
- [ ] Expand telemetry and diagnostic tools
- [ ] Complete the goBILDA CAD library
- [ ] Add automated testing and documentation

## 🔢 Versioning

To update the project version, run **README Version Bump** from the repository's
GitHub Actions tab on the branch to update. Choose **bug-fix**, **minor-update**,
or **major-update**; the workflow applies the corresponding patch, minor, or
major version increment and commits the README change to that branch. The
pre-release label remains while the project is in active development.

## 🤝 Contributing

Contributions from Steel Surge team members are welcome.

To contribute:

1. Create a new development branch.
2. Make and test your changes.
3. Follow existing FTC SDK and Java conventions.
4. Document significant changes.
5. Submit a pull request for review.

Always test motor direction, servo behavior, and emergency stopping before operating the robot at full power.

## 📚 Resources

- [FIRST Tech Challenge](https://www.firstinspires.org/robotics/ftc)
- [FTC Documentation](https://ftc-docs.firstinspires.org/)
- [FTC SDK](https://github.com/FIRST-Tech-Challenge/FtcRobotController)
- [REV Robotics](https://www.revrobotics.com/)
- [goBILDA](https://www.gobilda.com/)

## 📄 License

This project is licensed under the **MIT License**.

Copyright © 2026 Harvey Clauss.

See [LICENSE](LICENSE) for complete terms.

---

**FTC Steel Surge — Engineering. Innovation. Competition.**

*Built by students, powered by teamwork.*
