<h1 align="center">
  <span style="color:#7c3aed;">Smart Password Strength Analyzer</span>
</h1>

<p align="center">
  <b><span style="color:#22c55e;">Analyze.</span></b>
  <b><span style="color:#f59e0b;">Understand.</span></b>
  <b><span style="color:#38bdf8;">Upgrade.</span></b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-Swing%20Desktop-7c3aed?style=for-the-badge" alt="Java Swing Desktop">
  <img src="https://img.shields.io/badge/Password-Strength%20Analysis-22c55e?style=for-the-badge" alt="Password Strength Analysis">
  <img src="https://img.shields.io/badge/Adaptive-Recommendation%20Engine-f59e0b?style=for-the-badge" alt="Adaptive Recommendation Engine">
  <img src="https://img.shields.io/badge/DSA-Trie%20%7C%20HashSet%20%7C%20Entropy-06b6d4?style=for-the-badge" alt="DSA">
</p>

<p align="center">
  <i>A cyber-smart Java application that exposes weak passwords and transforms them into stronger, personalized alternatives.</i>
</p>

## <span style="color:#60a5fa;">Turn weak passwords into strong, intelligent choices</span>

**Smart Password Strength Analyzer** is a Java desktop application that does more than say "weak" or "strong".

It acts like a **security assistant**:

- analyzes the real quality of a password
- explains *why* it is weak, moderate, or strong
- estimates entropy and crack resistance
- detects risky patterns like repeats, sequences, and common words
- generates **adaptive password recommendations** based on the user's original input
- lets users **copy stronger recommended passwords directly from the UI**

<p>
  <b><span style="color:#a78bfa;">This project combines a polished Swing interface with algorithmic logic, data structures, and security thinking to create a tool that is practical, educational, and visually engaging.</span></b>
</p>

---

## <span style="color:#f472b6;">Why this app is different</span>

Most password checkers stop at a score.

This one goes further.

Instead of only warning the user, it helps them recover from a weak choice by generating **personalized stronger alternatives** that still feel familiar. If a user types something like `khumalo38@`, the system can recommend stronger variants inspired by the same core identity while improving length, complexity, and unpredictability.

That means the application is not just an analyzer.

<p>
  <b><span style="color:#22c55e;">It is a context-aware password improvement engine.</span></b>
</p>

---

## <span style="color:#38bdf8;">What the application does</span>

### <span style="color:#f97316;">1. Password strength analysis</span>

The analyzer evaluates:

- password length
- uppercase / lowercase / digit / symbol diversity
- entropy
- repeated characters
- sequential patterns
- keyboard-style patterns
- common words and unsafe substrings

It then returns:

- a score out of 100
- a strength label: `WEAK`, `MODERATE`, or `STRONG`
- estimated crack time
- detected issues
- improvement suggestions

### <span style="color:#22c55e;">2. Adaptive password generation</span>

When a password is weak or moderate, the app generates **3 to 5 stronger recommendations** that are still connected to the user's original password.

It does this by:

- extracting the core alphabetical base from the input
- capitalizing parts of the base
- appending randomized digits
- inserting symbols
- increasing total length
- shuffling internal characters in a controlled way
- ensuring character diversity across uppercase, lowercase, digits, and symbols

### <span style="color:#a78bfa;">3. Smart UI experience</span>

The Swing UI provides:

- a clean dark modern layout
- password analysis feedback
- adaptive recommendations in a dedicated section
- a **Copy** button next to each suggested password
- a full-page scroll experience for smaller screens

---

## <span style="color:#facc15;">Core features</span>

> <span style="color:#f8fafc; background-color:#111827;">Fast feedback. Smart explanations. Stronger alternatives. Better security decisions.</span>

- Real-time style password analysis workflow
- Strong scoring pipeline built with modular Java classes
- Entropy-based crack resistance estimation
- Pattern detection using data structures and algorithms
- Adaptive password generator with recognizable transformations
- Scrollable UI for smaller screens
- Copy-to-clipboard support for recommendations
- Modular object-oriented design for easy extension

---

## <span style="color:#fb7185;">Data structures and algorithms used</span>

This project is not just UI work. It is also a data-structures-and-algorithms-driven application.

### <span style="color:#22c55e;">`Trie`</span>

Used to store common passwords and risky dictionary-like substrings for fast matching inside user input.

### <span style="color:#60a5fa;">`HashMap`</span>

Used for pattern analysis such as character frequency tracking.

### <span style="color:#f59e0b;">`HashSet`</span>

Used for character pools in the recommendation engine:

- uppercase characters
- lowercase characters
- digits
- symbols

### <span style="color:#c084fc;">Sliding window pattern detection</span>

Used to detect:

- ascending sequences like `123` or `abc`
- descending sequences
- repeated runs like `aaa`

### <span style="color:#06b6d4;">Entropy calculation</span>

The app estimates entropy based on:

- password length
- effective character pool size

This gives a more meaningful measure than simple length checks alone.

### <span style="color:#f43f5e;">Controlled randomization</span>

Used in recommendation generation to create unique results while keeping the output recognizable and relevant to the original input.

---

## <span style="color:#818cf8;">Architecture</span>

The project is organized into focused modules.

### <span style="color:#22c55e;">Analysis layer</span>

- `src/dsa/ScoringEngine.java`
- `src/dsa/EntropyCalculator.java`
- `src/dsa/PatternDetector.java`
- `src/dsa/Trie.java`
- `src/dsa/AnalysisResult.java`

This layer handles password evaluation, scoring, entropy, pattern detection, and issue reporting.

### <span style="color:#f59e0b;">Recommendation layer</span>

- `src/recommendation/AdaptivePasswordGenerator.java`
- `src/recommendation/ContextAwarePasswordRecommendationEngine.java`
- `src/recommendation/PasswordWeaknessAnalyzer.java`
- `src/recommendation/PasswordSuggestion.java`
- `src/recommendation/PasswordPolicy.java`
- `src/recommendation/CharacterPools.java`
- `src/recommendation/...`

This layer handles password transformations, recommendation generation, safety filtering, and output formatting.

### <span style="color:#38bdf8;">UI layer</span>

- `src/PasswordAnalyzerUI.java`

This is the Swing desktop interface that connects user input, analysis output, and recommendations into one smooth workflow.

---

## <span style="color:#e879f9;">User experience flow</span>

```text
Enter password
    -> Analyze password
        -> Detect weaknesses and patterns
            -> Score + entropy + crack time
                -> Show issues and improvement tips
                    -> If weak/moderate, generate strong related alternatives
                        -> User copies recommended password
```

---

## <span style="color:#f97316;">Example scenario</span>

### <span style="color:#60a5fa;">Input</span>

```text
khumalo38@
```

### <span style="color:#fb7185;">The app may detect</span>

- not long enough
- predictable structure
- limited complexity compared to ideal strong standards

### <span style="color:#22c55e;">The app may recommend stronger variants like</span>

```text
KhumAlo227898##%
KhumaL0#78873^$$
KhuMalo62@781!#%
```

<p>
  <b><span style="color:#22c55e;">Each recommendation is designed to stay recognizable while being much harder to guess or brute-force.</span></b>
</p>

---

## <span style="color:#a78bfa;">Screens and interface highlights</span>

The interface is designed to feel modern and useful:

- dark, focused theme
- clear score and strength display
- easy-to-read issue and suggestion blocks
- recommendation list with copy buttons
- vertical page scrolling for smaller displays

This makes the application suitable for demonstrations, coursework, portfolios, and practical security education.

---

## <span style="color:#38bdf8;">How to run</span>

### <span style="color:#22c55e;">Option 1: Use the script</span>

From the project root:

```bash
bash run.sh
```

### <span style="color:#f59e0b;">What the script does</span>

- compiles all Java source files into `out/`
- launches the Swing application

### <span style="color:#c084fc;">Option 2: Compile manually</span>

```bash
javac -d out src/dsa/*.java src/recommendation/*.java src/PasswordAnalyzerUI.java
java -cp out PasswordAnalyzerUI
```

---

## <span style="color:#f472b6;">Project structure</span>

```text
.
├── run.sh
├── src
│   ├── PasswordAnalyzerUI.java
│   ├── PasswordAnalyzer.java
│   ├── dsa
│   │   ├── AnalysisResult.java
│   │   ├── EntropyCalculator.java
│   │   ├── PatternDetector.java
│   │   ├── ScoringEngine.java
│   │   └── Trie.java
│   └── recommendation
│       ├── AdaptivePasswordGenerator.java
│       ├── CharacterPools.java
│       ├── ContextAwarePasswordRecommendationEngine.java
│       ├── ControlledShuffleStrategy.java
│       ├── LengthAndDiversityStrategy.java
│       ├── PasswordPolicy.java
│       ├── PasswordSafetyFilter.java
│       ├── PasswordSuggestion.java
│       ├── PasswordWeaknessAnalyzer.java
│       ├── PasswordWeaknessReport.java
│       ├── RecommendationDemo.java
│       ├── SequenceBreakerStrategy.java
│       ├── SubstitutionStrategy.java
│       ├── TransformationResult.java
│       └── TransformationStrategy.java
└── out
```

---

## <span style="color:#facc15;">Best use cases</span>

- security awareness demos
- Java OOP and DSA coursework
- portfolio projects
- password education tools
- desktop utility experiments
- UI + algorithm integration showcases

---

## <span style="color:#22c55e;">What this project really represents</span>

This application is about more than checking passwords.

It is about helping users move from:

- weak choices
- guessable patterns
- short, familiar passwords

to:

- stronger habits
- safer alternatives
- smarter password creation

In short:

> <span style="color:#ffffff; background-color:#16a34a;"><b>Smart Password Strength Analyzer helps users understand password weakness and immediately upgrade to stronger, personalized alternatives.</b></span>

---

## <span style="color:#60a5fa;">Future ideas</span>

- live analysis while typing
- password history and export options
- strength visualization charts
- passphrase mode
- custom unsafe-word dictionaries
- themed UI polish and animations
- unit tests for scoring and generation modules

---

## <span style="color:#f97316;">Final note</span>

If you want a Java project that combines:

- clean UI
- object-oriented design
- algorithmic thinking
- practical cybersecurity value

<p>
  <b><span style="color:#a78bfa;">This project delivers all four in one powerful experience.</span></b>
</p>
