# Smart Password Strength Analyzer

## Turn weak passwords into strong, intelligent choices

**Smart Password Strength Analyzer** is a Java desktop application that does more than say "weak" or "strong".

It acts like a **security assistant**:

- analyzes the real quality of a password
- explains *why* it is weak, moderate, or strong
- estimates entropy and crack resistance
- detects risky patterns like repeats, sequences, and common words
- generates **adaptive password recommendations** based on the user's original input
- lets users **copy stronger recommended passwords directly from the UI**

This project combines a polished Swing interface with data structures and algorithmic logic to create a tool that is practical, educational, and visually engaging.

---

## Why this app is different

Most password checkers stop at a score.

This one goes further.

Instead of only warning the user, it helps them recover from a weak choice by generating **personalized stronger alternatives** that still feel familiar. If a user types something like `khumalo38@`, the system can recommend stronger variants inspired by the same core identity while improving length, complexity, and unpredictability.

That means the application is not just an analyzer.

It is a **context-aware password improvement engine**.

---

## What the application does

### 1. Password strength analysis

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

### 2. Adaptive password generation

When a password is weak or moderate, the app generates **3 to 5 stronger recommendations** that are still connected to the user's original password.

It does this by:

- extracting the core alphabetical base from the input
- capitalizing parts of the base
- appending randomized digits
- inserting symbols
- increasing total length
- shuffling internal characters in a controlled way
- ensuring character diversity across uppercase, lowercase, digits, and symbols

### 3. Smart UI experience

The Swing UI provides:

- a clean dark modern layout
- password analysis feedback
- adaptive recommendations in a dedicated section
- a **Copy** button next to each suggested password
- a full-page scroll experience for smaller screens

---

## Core features

- Real-time style password analysis workflow
- Strong scoring pipeline built with modular Java classes
- Entropy-based crack resistance estimation
- Pattern detection using data structures and algorithms
- Adaptive password generator with recognizable transformations
- Scrollable UI for smaller screens
- Copy-to-clipboard support for recommendations
- Modular object-oriented design for easy extension

---

## Data structures and algorithms used

This project is not just UI work. It is also a data-structures-and-algorithms-driven application.

### `Trie`

Used to store common passwords and risky dictionary-like substrings for fast matching inside user input.

### `HashMap`

Used for pattern analysis such as character frequency tracking.

### `HashSet`

Used for character pools in the recommendation engine:

- uppercase characters
- lowercase characters
- digits
- symbols

### Sliding window pattern detection

Used to detect:

- ascending sequences like `123` or `abc`
- descending sequences
- repeated runs like `aaa`

### Entropy calculation

The app estimates entropy based on:

- password length
- effective character pool size

This gives a more meaningful measure than simple length checks alone.

### Controlled randomization

Used in recommendation generation to create unique results while keeping the output recognizable and relevant to the original input.

---

## Architecture

The project is organized into focused modules.

### Analysis layer

- `src/dsa/ScoringEngine.java`
- `src/dsa/EntropyCalculator.java`
- `src/dsa/PatternDetector.java`
- `src/dsa/Trie.java`
- `src/dsa/AnalysisResult.java`

This layer handles password evaluation, scoring, entropy, pattern detection, and issue reporting.

### Recommendation layer

- `src/recommendation/AdaptivePasswordGenerator.java`
- `src/recommendation/ContextAwarePasswordRecommendationEngine.java`
- `src/recommendation/PasswordWeaknessAnalyzer.java`
- `src/recommendation/PasswordSuggestion.java`
- `src/recommendation/PasswordPolicy.java`
- `src/recommendation/CharacterPools.java`
- `src/recommendation/...`

This layer handles password transformations, recommendation generation, safety filtering, and output formatting.

### UI layer

- `src/PasswordAnalyzerUI.java`

This is the Swing desktop interface that connects user input, analysis output, and recommendations into one smooth workflow.

---

## User experience flow

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

## Example scenario

### Input

```text
khumalo38@
```

### The app may detect

- not long enough
- predictable structure
- limited complexity compared to ideal strong standards

### The app may recommend stronger variants like

```text
KhumAlo227898##%
KhumaL0#78873^$$
KhuMalo62@781!#%
```

Each recommendation is designed to stay **recognizable** while being much harder to guess or brute-force.

---

## Screens and interface highlights

The interface is designed to feel modern and useful:

- dark, focused theme
- clear score and strength display
- easy-to-read issue and suggestion blocks
- recommendation list with copy buttons
- vertical page scrolling for smaller displays

This makes the application suitable for demonstrations, coursework, portfolios, and practical security education.

---

## How to run

### Option 1: Use the script

From the project root:

```bash
bash run.sh
```

### What the script does

- compiles all Java source files into `out/`
- launches the Swing application

### Option 2: Compile manually

```bash
javac -d out src/dsa/*.java src/recommendation/*.java src/PasswordAnalyzerUI.java
java -cp out PasswordAnalyzerUI
```

---

## Project structure

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

## Best use cases

- security awareness demos
- Java OOP and DSA coursework
- portfolio projects
- password education tools
- desktop utility experiments
- UI + algorithm integration showcases

---

## What this project really represents

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

> **Smart Password Strength Analyzer helps users understand password weakness and immediately upgrade to stronger, personalized alternatives.**

---

## Future ideas

- live analysis while typing
- password history and export options
- strength visualization charts
- passphrase mode
- custom unsafe-word dictionaries
- themed UI polish and animations
- unit tests for scoring and generation modules

---

## Final note

If you want a Java project that combines:

- clean UI
- object-oriented design
- algorithmic thinking
- practical cybersecurity value

this project delivers all four in one experience.
