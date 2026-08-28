# Commit Message Rules (Project Standard)

These rules are mandatory for every commit message generated in this project.
 
---

## Rule 1 — Every commit has exactly two parts

A commit message consists of:

1. Heading
2. Body
   Format:

```
<Heading>
 
<Body>
```

There must always be one empty line between the heading and the body.
 
---

## Rule 2 — The heading answers ONLY one question

The heading answers:

**"What did we change?"**

Nothing else.

The heading must:

- describe the change
- use the imperative mood
- be concise
- start with a capital letter
- never explain the reason
- never explain implementation
- never end with a period
---

## Rule 3 — Select the correct action verb

The first word of the heading must accurately describe the nature of the change.

### Add

Use when introducing a completely new artifact or capability that did not previously exist. This includes adding tests for existing behaviour, since the test itself is a new artifact.

Examples:

- Add project README documentation
- Add Dragon API client
- Add configuration properties
- Add unit test for application processor
---

### Introduce

Use when introducing a new concept, strategy, workflow, architectural component, or design into the project.

Examples:

- Introduce quest ranking strategy
- Introduce purchase prioritization
- Introduce retry mechanism
  This is preferred over "Add" when the change represents a new idea rather than simply another file. **If a change could plausibly be described by both "Add" and "Introduce," prefer "Introduce."**

---

### Implement

Use when providing the actual implementation of an already planned or existing feature, interface, or design.

Examples:

- Implement inventory management
- Implement game state synchronization
- Implement purchase strategy
---

### Update

Use when modifying existing functionality without a clear claim that it is now *better* — the purpose and quality bar stay the same, only the behaviour or configuration changes.

Examples:

- Update quest selection strategy
- Update API configuration
---

### Improve

Use when making existing functionality quantitatively or qualitatively better (faster, more accurate, more robust, more correct) without changing its purpose.

Examples:

- Improve error handling
- Improve quest prioritization
- Improve test coverage
  **Tiebreaker (Update vs. Improve):** if the change is primarily about making something better along some measurable or qualitative dimension, use "Improve." If it changes what or how something works without a clear "better" claim, use "Update."

---

### Refactor

Use when restructuring code without changing observable behaviour.

Examples:

- Refactor game context mapping
- Refactor quest ranking logic
---

### Rename

Use when names change.

Examples:

- Rename reliability score to risk score
---

### Remove

Use when deleting functionality or code.

Examples:

- Remove obsolete utility methods
- Remove unused configuration
---

### Fix

Use only when correcting incorrect behaviour or bugs.

Examples:

- Fix quest expiration handling
- Fix purchase decision logic
---

### General Principle

Choose the verb that best answers:

**"What kind of change did this commit make?"**

Not every commit should start with **Implement**.

Examples:

- New README → Add
- New strategy → Introduce
- Coding the strategy → Implement
- Better algorithm → Improve
- Internal cleanup → Refactor
- Bug correction → Fix
- New test for existing behaviour → Add
---

## Rule 4 — The body answers ONLY one question

The body answers:

**"Why was this change made?"**

Nothing else.

The body must:

- explain the motivation
- explain the engineering reason
- explain the business value when applicable
- never describe implementation details
- never describe code structure
- never mention classes, methods, or APIs unless absolutely necessary to explain the reason
  
Good:

- Provide setup and usage instructions so the project can be easily run and evaluated.
- Ensure quest selection no longer depends on the order returned by the Dragons of Mugloar API.
- Reduce the likelihood of selecting quests with poor observed outcomes.
  
Bad:

- Uses Comparator.comparingInt().
- Added a map.
- Introduced QuestRankingStrategy.
- Uses Map.ofEntries().
- Sorts using QUEST_RISK_SCORE.
---

## Rule 5 — Never explain HOW

The commit message never explains implementation. Implementation belongs in the code.

Never write:

- added comparator
- introduced lambda
- created map
- uses stream()
- implemented method
- changed if statement
  
Instead explain the purpose.

---

## Rule 6 — Focus on intent

Every commit message should explain intent.

Ask yourself:

- What changed? → Heading
- Why was it necessary? → Body
  
Never answer: How was it implemented?

---

## Rule 7 — Prefer engineering outcomes

Prefer:

- Make quest selection deterministic.
- Reduce dependence on API response ordering.
- Provide project documentation.
- Prevent unsupported quests from being selected.


  Avoid:

- Added comparator.
- Created strategy class.
- Added README file.
- Implemented sorting.
---

## Rule 8 — Keep messages concise

Heading: usually 3–8 words.
Body: usually 1–2 sentences.

Avoid unnecessary detail. No hard character limit is enforced — use judgment.
 
---

## Rule 9 — One logical change per commit

Each commit should represent a single logical change. If the heading needs "and" to describe it, treat that as a signal to split the change into separate commits rather than writing a compound heading.
 
---

## Rule 10 — Do not repeat the heading

The body should never restate the heading.

Bad:

```
Add README documentation
 
This adds README documentation.
```

Good:

```
Add README documentation
 
Provide setup and usage instructions so the project can be easily run and evaluated.
```
 
---

## Rule 11 — Default writing style

Use clear professional English.

Avoid:

- marketing language
- exaggerated wording
- implementation jargon
- unnecessary adjectives

  Prefer factual statements.

---

## Rule 12 — Default quality check

Before finalizing every commit message verify:

- ✓ Heading explains WHAT changed.
- ✓ Heading uses the correct action verb.
- ✓ Heading starts with a capital letter and has no trailing period.
- ✓ Heading is a single logical change (no "and").
- ✓ Body explains WHY it changed.
- ✓ Body does not explain HOW.
- ✓ No implementation details.
- ✓ Concise.
- ✓ Professional.
- ✓ Heading and body answer different questions.
---

# Examples

### Example 1

```
Add project README documentation
 
Provide setup and usage instructions so the project can be easily run and evaluated.
```

### Example 2

```
Introduce quest ranking strategy
 
Make quest selection independent of the order returned by the Dragons of Mugloar API.
```

### Example 3

```
Implement quest ranking strategy
 
Provide the application with a deterministic way to prioritize quests based on observed historical outcomes.
```

### Example 4

```
Improve quest prioritization
 
Reduce the likelihood of selecting quests with weaker observed outcomes.
```

### Example 5

```
Filter encrypted quests
 
Prevent quests that cannot be completed from being considered during game progression.
```

### Example 6

```
Add unit test for application processor
 
Verify that repayment schedules are calculated correctly for supported loan types.
```
 
