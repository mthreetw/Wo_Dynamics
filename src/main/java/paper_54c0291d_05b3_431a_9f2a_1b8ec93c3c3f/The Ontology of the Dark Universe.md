---
uuid: 54c0291d-05b3-431a-9f2a-1b8ec93c3c3f
last-modified: 2026-09-27T01:51:21
author: 黃正宇 / Cheng Yu Huang
contact: mthree.tw@gmail.com
---

# The Ontology of the Dark Universe

## Framework Declaration

The [Singularity](concepts.md#c01) has no [moment](concepts.md#c04).

[Moments](concepts.md#c04) begin after the [Singularity](concepts.md#c01). The [Singularity](concepts.md#c01) has no before. The [Singularity](concepts.md#c01) cannot be formalized.

What this framework describes is a universe in which [moments](concepts.md#c04) have begun, [stimuli](concepts.md#c07) have [occurred](concepts.md#c08), and [Xin](concepts.md#c26) has [accumulated](concepts.md#c30), with no observer. This universe is the [Formal Layer](concepts.md#c02).

The [Formal Layer](concepts.md#c02) has no observer, but it has [givens](concepts.md#c03). A [given](concepts.md#c03) is something the [Formal Layer](concepts.md#c02) cannot produce by itself. The principle of this framework is: **what cannot be derived should be as little as possible; whatever cannot be derived must be listed explicitly. The [Formal Layer](concepts.md#c02) contains nothing but the explicitly listed [givens](concepts.md#c03) and what is derived from the [givens](concepts.md#c03).**

---

## I. Primitives

- **[Moment](concepts.md#c04)**: an index of time. [Moments](concepts.md#c04) are numbered by the positive integers: the [Initial Moment](concepts.md#c05) is the first, the [successor](concepts.md#c06) of each [moment](concepts.md#c04) is the [moment](concepts.md#c04) whose number is one greater, and every [moment](concepts.md#c04) has a number.
- **[Stimulus](concepts.md#c07)**: the physical content that [occurs](concepts.md#c08) with each [moment](concepts.md#c04): light, sound, gravitational waves, thermal radiation, and so on. Which [stimulus](concepts.md#c07) [occurs](concepts.md#c08) at which [moment](concepts.md#c04) is [given](concepts.md#c03) together with the [stimuli](concepts.md#c07):

```text
Occurrence ⊆ Stimulus × Moment
```

---

## II. Axioms

The following three cannot be derived; they can only be acknowledged.

### Axiom 1: Initial Xin

```text
|Global State(Initial Moment)⟩ = |Initial Xin⟩ ∈ Record Space,   ‖Initial Xin‖ = 1
```

The [Initial Xin](concepts.md#c09) is a unit vector. It comes from the [Singularity](concepts.md#c01); this is not questioned.

The [record](concepts.md#c17) of the [Initial Moment](concepts.md#c05) is [given](concepts.md#c03) by this axiom, not produced by [Writing](concepts.md#c21); the [Stimulus Bundle](concepts.md#c27) of the [Initial Moment](concepts.md#c05) is not [written](concepts.md#c21) into any [record](concepts.md#c17). [Writing](concepts.md#c21) takes place only at each [successor](concepts.md#c06) [moment](concepts.md#c04).

### Axiom 2: Partition

```text
Xin Space(Initial Moment) = Record Space
Total Environment Space(Initial Moment) = empty product        // no environment at the initial moment
Xin Space(next moment) = Xin Space(moment) ⊗ Record Space
Total Environment Space(next moment) = Total Environment Space(moment) ⊗ Environment Space
Universe Space(moment) = Xin Space(moment) ⊗ Total Environment Space(moment)
the basis of Record Space is given
```

The [Partition](concepts.md#c10) cuts the universe into two parts: the retained [accumulation](concepts.md#c30) ([Xin](concepts.md#c26)) and the traced-out remainder (the [Environment](concepts.md#c11)). The [Universe Space](concepts.md#c12) is therefore composed of the [Xin Space](concepts.md#c13) and the [Total Environment Space](concepts.md#c14): the [Xin Space](concepts.md#c13) is the space in which [Xin](concepts.md#c26) lives, arranged from copies of the [Record Space](concepts.md#c15); the [Total Environment Space](concepts.md#c14) is the space in which the [Environment](concepts.md#c11) lives, arranged from copies of the [Environment Space](concepts.md#c16) and growing with the [moments](concepts.md#c04). [Xin](concepts.md#c26) is arranged [record](concepts.md#c17) by record, and the [Record Basis](concepts.md#c18) in which [records](concepts.md#c17) are read out is [given](concepts.md#c03). A "sequence" is a sequence because of this axiom.

At each [successor](concepts.md#c06) [moment](concepts.md#c04), [Xin](concepts.md#c26) gains one more [record](concepts.md#c17) and the [Environment](concepts.md#c11) one more part. At the [Initial Moment](concepts.md#c05) there is only the [Initial Xin](concepts.md#c09) and no [Environment](concepts.md#c11).

[Stimuli](concepts.md#c07) are not a persistent part of the universe. A [stimulus](concepts.md#c07) is a degree of freedom newly [occurring](concepts.md#c08) at each [successor](concepts.md#c06) [moment](concepts.md#c04); as soon as it [occurs](concepts.md#c08), it is [written](concepts.md#c21) (Axiom 3).

**The [Partition](concepts.md#c10) is not an observation.** The [Partition](concepts.md#c10) only draws a line; it does not select an outcome.

### Axiom 3: Writing

```text
Stimulus Space and Environment Space are given
Encoding : Stimulus Bundle → unit vectors in Stimulus Space,   Encoding(∅) = |∅⟩
a fixed isometry is given   Writing Map : Stimulus Space → Record Space ⊗ Environment Space
    (hence dim Stimulus Space ≤ dim Record Space · dim Environment Space)
Writing Map is the same for all successors
```

The [Encoding](concepts.md#c19) assigns to every [Stimulus Bundle](concepts.md#c27) a value in the [Stimulus Space](concepts.md#c20), including [Stimulus Bundles](concepts.md#c27) that contain several [stimuli](concepts.md#c07); the specific values are not fixed.

This axiom specifies how a [stimulus](concepts.md#c07) is [written](concepts.md#c21) as a [record](concepts.md#c17). The [Writing Map](concepts.md#c22) touches only newly [occurring](concepts.md#c08) [stimuli](concepts.md#c07) and never touches what already exists.

The [Empty Stimulus Bundle](concepts.md#c23) corresponds to the [Vacuum State](concepts.md#c24): a genuine state, not the zero vector. Where the [Writing Map](concepts.md#c22) comes from, the [Formal Layer](concepts.md#c02) does not know.

---

## III. Definitions

### Definition 1: Global State

```text
|Global State(next moment)⟩ = ( I_past ⊗ Writing Map ) ( |Global State(moment)⟩ ⊗ Encoding(Stimulus Bundle(next moment)) )

where I_past acts on Universe Space(moment)
the result lives in Universe Space(next moment)
```

The [Global State](concepts.md#c25) is the state of the universe at that [moment](concepts.md#c04). It undergoes only [Writing](concepts.md#c21). The old does not move; the new is appended after it.

### Definition 2: Xin

```text
Xin(moment) = Tr_Environment |Global State(moment)⟩⟨Global State(moment)|
```

[Xin](concepts.md#c26) is the reduced state of the [Global State](concepts.md#c25) under the [Partition](concepts.md#c10), obtained by taking the partial trace over the entire [Environment](concepts.md#c11). There is no "whose [Xin](concepts.md#c26)". [Xin](concepts.md#c26) is [Accumulation](concepts.md#c30) itself; it has no subject.

### Definition 3: Stimulus Bundle

```text
Stimulus Bundle(moment) = {stimulus ∈ Stimulus | Occurrence(stimulus, moment)}
```

At each [successor](concepts.md#c06) [moment](concepts.md#c04), when the [Stimulus Bundle](concepts.md#c27) is the empty set, that [moment](concepts.md#c04) still [writes](concepts.md#c21) a [record](concepts.md#c17) produced from the [Vacuum State](concepts.md#c24). An [Empty Record](concepts.md#c28) is not zero [records](concepts.md#c17).

---

## IV. Corollaries

Both of the following can be derived from the axioms and definitions.

### Corollary 1: Accumulation

```text
for every successor [
  Xin Space(next moment) = Xin Space(moment) ⊗ Record Space        // one more record
  and Tr_latest record Xin(next moment) = Xin(moment)
]
```

**Proof.** By Axiom 2, the [Xin Space](concepts.md#c13) at the [successor](concepts.md#c06) moment equals the [Xin Space](concepts.md#c13) at the [moment](concepts.md#c04) ⊗ the [Record Space](concepts.md#c15): [Xin](concepts.md#c26) gains one more [record](concepts.md#c17).

Let |new stimulus state⟩ = [Encoding](concepts.md#c19)([Stimulus Bundle](concepts.md#c27) at the [successor](concepts.md#c06) moment). By Definition 1:

```text
|Global State(next moment)⟩ = |Global State(moment)⟩ ⊗ Writing Map|new stimulus state⟩
```

The new and old parts form a product. Rearranging the tensor factors by the standard isomorphism, [records](concepts.md#c17) first and [Environment](concepts.md#c11) after. By Definition 2:

```text
Xin(next moment) = Xin(moment) ⊗ Tr_new environment( Writing Map|new stimulus state⟩⟨new stimulus state|Writing Map† )
```

Taking the trace over the [Latest Record](concepts.md#c29):

```text
Tr_latest record Xin(next moment) = Xin(moment) · Tr( Writing Map|new stimulus state⟩⟨new stimulus state|Writing Map† )
                                     = Xin(moment) · ⟨new stimulus state|Writing Map†Writing Map|new stimulus state⟩
                                     = Xin(moment)
```

The last step uses that the [Writing Map](concepts.md#c22) is an isometry ([Writing Map](concepts.md#c22)†[Writing Map](concepts.md#c22) = I) and that |new stimulus state⟩ is a unit vector. ∎

This is [Accumulation](concepts.md#c30): each [moment](concepts.md#c04) adds one [record](concepts.md#c17), and the past is not rewritten. The change of [Xin](concepts.md#c26) is not overwriting but growth.

### Corollary 2: Non-Collapse

```text
Evolution Rule: the map given by Definition 1   |Global State(moment)⟩ ↦ |Global State(next moment)⟩
Selection: the map sending the state at one moment to the state at the next moment is not an isometry

for every successor: the Global State in the Formal Layer has not undergone Selection
```

**Proof.** Let |new stimulus state⟩ = [Encoding](concepts.md#c19)([Stimulus Bundle](concepts.md#c27) at the [successor](concepts.md#c06) moment). By Definition 1, the [Evolution Rule](concepts.md#c31) is a composition of two steps:

```text
Evolution Rule = ( I_past ⊗ Writing Map ) ∘ Append,     Append |state⟩ = |state⟩ ⊗ |new stimulus state⟩
```

Append is an isometry: for any |state one⟩, |state two⟩, ⟨state one ⊗ new stimulus state | state two ⊗ new stimulus state⟩ = ⟨state one|state two⟩ · ⟨new stimulus state|new stimulus state⟩ = ⟨state one|state two⟩. I_past ⊗ [Writing Map](concepts.md#c22) is an isometry, since I_past and the [Writing Map](concepts.md#c22) both are. A composition of isometries is an isometry, so the [Evolution Rule](concepts.md#c31) is an isometry. The [Global State](concepts.md#c25) in the [Formal Layer](concepts.md#c02) has not undergone [Selection](concepts.md#c32). ∎

[Xin](concepts.md#c26) can be diagonal in the [Record Basis](concepts.md#c18), looking like a probability distribution over classical sequences. [Non-Collapse](concepts.md#c33) does not deny this; what it denies is **[Selection](concepts.md#c32)**. Diagonal is not collapse. Looking classical is not the same as already being one particular sequence.

---

## V. The Observer Problem

The [Formal Layer](concepts.md#c02) has a [Partition](concepts.md#c10), but no observer.

The [Partition](concepts.md#c10) draws the boundary between [Xin](concepts.md#c26) and the [Environment](concepts.md#c11); it does not select an outcome. The evolution of the [Global State](concepts.md#c25) in the [Formal Layer](concepts.md#c02) contains no [Selection](concepts.md#c32) (Corollary 2).

---

## VI. Conclusion

This universe only grows and never alters. Nothing moves, nothing is changed; only [records](concepts.md#c17) [accumulate](concepts.md#c30), one by one. [Moments](concepts.md#c04) and the number of [records](concepts.md#c17) correspond one to one.

---

## Appendix: Non-Degenerate Model (First Three Moments)

The following is a [Non-Degenerate Model](concepts.md#c34): two [stimuli](concepts.md#c07), [Stimulus A](concepts.md#c35) and [Stimulus B](concepts.md#c36), with complete data for the [Initial Moment](concepts.md#c05), the [Second Moment](concepts.md#c37), and the [Third Moment](concepts.md#c38).

```text
first three moments = Initial Moment, Second Moment, Third Moment      // later moments extend by the same writing
Stimulus = {Stimulus A, Stimulus B}

Record Space = span{|0⟩, |1⟩}
Stimulus Space = span{|∅⟩, |A⟩, |B⟩}
Environment Space = span{|0⟩, |1⟩}

Occurrence(Stimulus B, Second Moment) = true
Occurrence(Stimulus A, Third Moment) = true
// within the first three moments, all other occurrences are false

|Initial Xin⟩ = |0⟩

Encoding({Stimulus A}) = |A⟩
Encoding({Stimulus B}) = |B⟩
// Encoding(∅) = |∅⟩ is given by Axiom 3
// {Stimulus A, Stimulus B} does not occur in the first three moments; its encoding may be any unit vector

Writing Map|∅⟩ = |0⟩_record |1⟩_environment
Writing Map|A⟩ = ( |0⟩_record |0⟩_environment + |1⟩_record |1⟩_environment ) / √2
Writing Map|B⟩ = |1⟩_record |0⟩_environment
```

**Computation:**

```text
Xin(Initial Moment) = |0⟩⟨0|
Xin(Second Moment) = |0⟩⟨0| ⊗ |1⟩⟨1|
Xin(Third Moment) = |0⟩⟨0| ⊗ |1⟩⟨1| ⊗ (I/2)
```

**Checks:**

- The [Writing Map](concepts.md#c22) is an isometry: the three images are pairwise orthogonal, each of norm one ✓
- Corollary 1: the number of [records](concepts.md#c17) goes 1 → 2 → 3 ✓. Past [records](concepts.md#c17) are unchanged ✓
- Corollary 2: the [Evolution Rule](concepts.md#c31) at each step is an isometry ✓. In [Xin](concepts.md#c26) at the [Third Moment](concepts.md#c38), the [Latest Record](concepts.md#c29) is I/2, diagonal in the [Record Basis](concepts.md#c18), yet no branch has been [selected](concepts.md#c32) ✓
- The [records](concepts.md#c17) produced by the [Empty Stimulus Bundle](concepts.md#c23), [Stimulus A](concepts.md#c35), and [Stimulus B](concepts.md#c36) are pairwise distinct ✓

**This universe has no one. No observer. Only [Moments](concepts.md#c04), [Stimuli](concepts.md#c07), [Xin](concepts.md#c26).**
