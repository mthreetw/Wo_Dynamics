---
uuid: 96f10d75-23d0-441f-94c7-ab87ca28d284
last-modified: 2026-10-22T12:08:04
author: 黃正宇 / Cheng Yu Huang
contact: mthree.tw@gmail.com
---

# Production Dynamics

## 0. Positioning and Assumptions

This paper is a programmatic paper that builds on upstream and introduces no new mechanism. On top of the upstream cycle of [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28), [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10), [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13), and [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06), it adds one layer of researcher's records, describing the [solution](concepts.md#c05) of each [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) in every [derivation round](concepts.md#c07): its [state](concepts.md#c02) and [resources](concepts.md#c03) at the next moment, and whether it [undergoes criterion exit](concepts.md#c09). To downstream papers (for example, a paper on violence dynamics) it offers only a bookkeeping format: a [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) has [state variables](concepts.md#c02) and [resource variables](concepts.md#c03); [resource variables](concepts.md#c03) increase or decrease in every [derivation round](concepts.md#c07); the [exit criterion](concepts.md#c04) is written on [state variables](concepts.md#c02).

<note>

*This paper is a transitional programme: it invents no new mechanism and only gives later papers a bookkeeping format. Each node records two kinds of numbers (state and resources); resources go up and down in every derivation round, and whether a node undergoes criterion exit is decided by a criterion written on the state.*

</note>

[Production](concepts.md#c01) is the researcher's [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) drawn over the [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) of [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28), and it gives [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) no independent ontology. [Resources](concepts.md#c03), [states](concepts.md#c02), and [criterion exit](concepts.md#c09) are all [distinctions](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) made by the researcher, and this paper presupposes no relation among the three.

<note>

*"Production" is not a property of output itself; it is how the researcher looks at that stretch of output history. Resource, state, and criterion exit are likewise just the researcher's divisions, and this paper does not assume that they are related to one another.*

</note>

The "Calibration" in each section and the cases in Chapter 6 are only comparisons with reality; they are no part of any derivation and are never a source of derivation.

<note>

*Calibrations and cases are only comparisons with reality, not proof of anything.*

</note>

What this paper calls the [environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16) is the upstream [natural environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16): under a stated [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23), the part that belongs to no [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) yet has [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) [arriving](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25).

<note>

*When the text says "environment", it means the upstream natural environment, not the "environment" of the dark-universe paper.*

</note>

---

## 1. Laws and Premises

**Inherited.** The [Law of Flux](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c24), the [Law of the Black Box](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c25), the [Law of Attrition](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c26), and the [Law of Non-Restoration](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c28); [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), [self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19), [entry](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c44), [exit](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43), [death](../paper_fb0addca_42b6_4884_aa18_438c695747ab/concepts.md#c22), [own segment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17), [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23), [natural environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16), [resonance](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c09), [culture](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c13), and so on are used with their upstream definitions and are not redefined.

<note>

*Things already defined upstream are used directly here and not explained again.*

</note>

```text
Premise 1 (scope setting): Under a stated cut, the researcher specifies the
     boundaries of the selected nodes, their state variables, their resource
     variables, and one exit criterion expressed in the state variables, and
     reports who made this division. This paper presupposes no relation among
     resources, states, and criterion exit.
Remark: Premise 1 is a scope setting, not a claim that can be refuted.
     Its scope is not limited to humans; from a human viewpoint, the exit
     criterion is often written on physiological state, and some self marks
     the wo as dead accordingly;
     criterion exit follows the criterion the researcher states,
     so it may or may not coincide with upstream exit, and the two do not substitute for each other.
     The criterion may be the upstream "no longer a wo", or any explicitly
     stated definition such as a physiological threshold, dissolution, or retirement.
Remark: The line between resource and state depends on the node boundary and
     has no fixed attribution belonging to the object itself.
     Transferability and storability are common cues, not definitions:
     what is transferable and storable from a given node's viewpoint is
     usually recorded as a resource, and what is not is usually recorded as a state.
     A researcher may place a variable related to some object under resource
     or state, depending on the node and the viewpoint of the cut
     (for example a prosthetic eye, or a worker's labour as seen by the
     worker versus by the boss).
Remark: The relation among resources, states, and criterion exit is not presupposed here.
     Both ends are the researcher's distinctions;
     at the level of general rules nothing can be written about it; if there is
     any, it is an empirical question under a specific cut,
     to be raised and borne by downstream research.
Step 1-1: The exit criterion is one distinction made by the researcher
     (from Premise 1 + definition of cut + definition of distinction + definition of exit criterion)
Step 1-2: Once the criterion is stated, whether a given derivation round
     has a criterion exit is decided by the actual state events of that round
     (from Premise 1 + Premise 2 + Step 1-1)
Conclusion 1-3: The criterion is a definition and whether criterion exit occurs is a fact;
     the two do not replace each other
     (from Step 1-1 + Step 1-2 + definition of exit criterion)
Step 1-4: What counts as the state or the resources of a node depends on the
     node boundary, and the boundary is one distinction made by the researcher
     (from Premise 1 + definition of distinction + definition of identity + definition of state variable + definition of resource variable)
Conclusion 1-5: The classification of resource and state is a classification
     under a cut, not a property of the thing itself
     (from Premise 1 + Step 1-4)
```

<note>

*Premise 1 sets the scope: the researcher first states how to cut, which numbers to record, and what to use as the standard for criterion exit. It is not a claim that can be proved right or wrong. The following lines are drawn directly from it.*

</note>

**Calibration.** Retirement counts as [criterion exit](concepts.md#c09), a physiological state reaching a specified threshold also counts as [criterion exit](concepts.md#c09) (often marked as [death](../paper_fb0addca_42b6_4884_aa18_438c695747ab/concepts.md#c22) by some [self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)), and the dissolution of a company is likewise [criterion exit](concepts.md#c09); each is defined by the user, so the [criterion](concepts.md#c04) is a definition, and whether a given person actually retires or reaches that threshold in a given [derivation round](concepts.md#c07) is a fact. Whether a prosthetic eye counts as part of him can be answered differently by the eye's user and the insurance company; a worker's mood and stamina are [state variables](concepts.md#c02) for the worker himself, while the labour the worker puts in is a [resource variable](concepts.md#c03) the boss can obtain. This is a calibration, not a source of derivation.

<note>

*In everyday terms: "what counts as criterion exit" is something we stipulate, while "whether this person actually retired or died today" is whether it happened. The same thing can be booked as a state in one person's account and as a resource in another's.*

</note>

### Prohibition list (methodologically forbidden)

1. Do not treat the [criterion](concepts.md#c04) as a fact, and do not treat an inference about [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) as a judgment.
2. Do not infer, from the record of increases and decreases of a [resource variable](concepts.md#c03), which kind of [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) an increase or decrease belongs to, or infer motive or legitimacy.
3. Do not add up [resource variables](concepts.md#c03) and [state variables](concepts.md#c02) across [cuts](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23).
4. Do not infer optimality from survival, or improvement from variation.
5. Do not presuppose antecedents about ownership or legitimacy.
6. Do not treat a particular classification as a property of the thing itself (for example "the eye is a state" or "money is a resource"); it must be stated together with the [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23).
7. Do not presuppose a relation between [resources](concepts.md#c03) and [criterion exit](concepts.md#c09); to claim one, first define the specific [resource](concepts.md#c03) and the specific [exit criterion](concepts.md#c04).
8. Do not speak of resources "moving": moving presupposes [identity](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c21), and [identity](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c21) is a [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18); this paper only records the increases and decreases of each [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)'s [resource variables](concepts.md#c03).
9. Do not treat "screening" or "elimination" as purposeful processes; they are only metaphors.

<note>

*Nine "do not infer this way" rules; each one is there to stop the books from being read as motive, causation, or value judgment.*

</note>

---

## 2. The Model

The unit by which this paper advances the model one step is called a [derivation round](concepts.md#c07): under a stated [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23), every [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) present runs once through the upstream cycle, after which come the researcher's records and the change of membership. The unit is the whole cut-out range, not a single [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25); what is recorded and how membership changes are specified by the user; the [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) are not assumed to be synchronous, and a [derivation round](concepts.md#c07) is only an ordering.

<note>

*"Derivation round" is this paper's own coinage: the model moves one step forward, everyone does what they are to do once, the researcher writes it down, and some undergo criterion exit while others enter. It is not the same thing as upstream "round": a round covers only one wo's own turn, while a derivation round covers one whole step of the entire range.*

</note>

The model of this paper is a recursion advanced by [derivation rounds](concepts.md#c07), following the upstream cycle. In every [derivation round](concepts.md#c07), each [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) produces [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28), the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) [arrives](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at itself, at other [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25), and at the [environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16), and the physical changes that [arrive](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) enter the [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13). **Finding the [solution](concepts.md#c05) means this: I [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) now, and what I become at the next moment is the [solution](concepts.md#c05) of this [derivation round](concepts.md#c07).** One person or many use the same model: with many, the researcher merely cuts out of the [environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16) a part that used to sit there and treats it as [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25), and the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of each [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) enters the next moment of the others.

<note>

*The whole model in one sentence: what I do now, and what I turn into at the next moment, that "what I turn into" is this round's answer. One person or many follow the same formula; the only difference is whether the researcher has also cut the others into nodes.*

</note>

```text
Premise 2 (derivation round structure): every derivation round proceeds in the following six steps
Round step 1  Output    Each node present, according to the weight determined by its own history, produces output.   (upstream)
Round step 2  Arrival   Output arrives at the node itself, and also at other nodes and the environment.   (upstream)
Round step 3  Response  The natural environment responds; other nodes also each produce output. All of these may arrive at this node.
                  What is inside the natural environment is not asked here.
Round step 4  Entry into history   All physical changes arriving at the node, whether from its own output,
                  from other nodes' output, or from the environment's response, enter the history of that node
                  and take part in later weight.
                  Weight is determined by history and cannot be read.   (upstream)
Round step 5  Solution  Under the cut, the researcher records each node's state variables and resource
                  variables at the next moment, and records whether each node undergoes criterion exit according to the
                  exit criterion. Taken together these are the solution of this derivation round.
Round step 6  Membership change   Nodes that undergo criterion exit no longer take part, but their history remains; nodes that enter join,
                  and their prior history may overlap with that of existing nodes.
Return to round step 1.
The cycle of this paper has no built-in exit condition; whether to stop is specified by the researcher outside the model.
```

<note>

*The six actions of one derivation round: everyone first produces output, the output arrives everywhere, the environment and the others respond, everything enters history, then the researcher writes down next-moment state, resources, and whether anyone undergoes criterion exit (this is the "solution"), and finally some criterion exit and some enter, and the next derivation round begins.*

</note>

```text
Definition of solution: Under a stated cut, the next-moment state variables and resource
     variables of each node that the researcher records, together with whether each node
     undergoes criterion exit according to the exit criterion, taken together form the solution of this derivation round.
Remark: Criterion exit is only one dimension of the solution, alongside the other dimensions, with no special status.
Remark: What is not recorded is not in the solution, but remains in history.
```

<note>

*"Solution" is the bundle of numbers the researcher writes down for this derivation round. What is not written down is not in it. Criterion exit is just one column.*

</note>

**Remarks**

1. The [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) contains all physical changes that [arrive](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25). The part that was not recorded remains in the [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) and continues to affect [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06).
2. The [solution](concepts.md#c05) presupposes no optimum, and presupposes no tie to purposes or resources. Whether a [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) [undergoes criterion exit](concepts.md#c09) is only a limit that reality gives: whether a person is hit by a car and dies at the next moment has no necessary relation to what he wants or how many resources he has.
3. If purposes show up as a [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)'s [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) about itself, they enter the [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) and affect later [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) only through [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06); this paper neither confirms nor presupposes their content (inheriting the upstream [unconfirmability of drives](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c03)).
4. With many people, the [solution](concepts.md#c05) of one [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) may be affected both by its own [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) and by the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of other [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25); the next moments of all [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) taken together are the [solution](concepts.md#c05) of the whole cut-out range. Each person produces [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) on their own, and each one's [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) enters the next moment of the others, so finding the [solution](concepts.md#c05) is a process that happens jointly among many people, not a matter of everyone keeping separate books.
5. The records of this paper cover only physical changes. [Distinctions](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) (views about oneself or others) also enter the [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13), as upstream has it, and are not recorded in this paper.
6. The [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) are not assumed to be synchronous; a [derivation round](concepts.md#c07) is only an ordering, as upstream has it.
7. This paper does not specify what the response is or how much the increase or decrease is. The same [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) may meet different responses today and tomorrow.
8. The cycle of each [derivation round](concepts.md#c07) of this paper has no built-in exit condition; whether to stop is specified by the researcher outside the model. [Criterion exit](concepts.md#c09) concerns individual [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) only and is not a termination condition for the whole.

<note>

*Eight additions: a record is not the whole history; a solution does not mean the optimum; purposes are not guessed; with many people everyone may affect everyone; views also enter history but are not recorded here; no synchrony is required; and nothing is specified about how the environment responds; when the whole derivation stops is specified by the researcher outside the model, and criterion exit is not a termination condition.*

</note>

**Calibration.** I go mining and my [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) is labour; the mine responds, and at the next moment I hold an extra piece of ore with my stamina lower; that is the [solution](concepts.md#c05) of this [derivation round](concepts.md#c07) for me. Digging the same way tomorrow, the ore may not appear. Another person is walking down the road to drink and is hit by a car and dies: in his [solution](concepts.md#c05), the dimension of whether he [undergoes criterion exit](concepts.md#c09) is recorded as [criterion exit](concepts.md#c09); this has nothing to do with what he wants or how many resources he has, and is only a limit given by reality. This is a calibration, not a source of derivation.

<note>

*Two everyday examples: when mining, there is ore today and maybe none tomorrow; being hit by a car has nothing to do with whether one wants a drink or has money.*

</note>

### Components

| Component | Content | Basis |
|---|---|---|
| Component 1: upstream cycle | [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28), [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10), entry into history, [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06), [entry](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c44), [exit](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43) | inherited, nothing new |
| Component 2: record of the [solution](concepts.md#c05) | the researcher records next-moment [state variables](concepts.md#c02), [resource variables](concepts.md#c03), and whether [criterion exit](concepts.md#c09) occurs | Premise 1, Premise 3, Premise 4 |

<note>

*The model has only two blocks: the ready-made upstream cycle, and the record the researcher adds.*

</note>

### Naming with many people

With many people, the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of each [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) enters the next moment of the others. On the [production](concepts.md#c01) dimension, if the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of these [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) [arrive](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at one another and, within a stated time window, their sequences repeatedly continue in a compatible way, that is what upstream calls [resonance](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c09); if the conditions of upstream [culture](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c13) are also met, the compatible pattern that [resonance](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c09) leaves behind is the manifestation, on the [production](concepts.md#c01) dimension, of what upstream calls [culture](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c13). [Resource variables](concepts.md#c03) are recorded variables and take no part in [resonance](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c09). Exchange, transfer, cooperation, plunder, and the like are the researcher's names for such [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) sequences; they are not components of the model and cannot be read off the books (see 5-6).

<note>

*With many people, if everyone's output keeps fitting together, upstream calls it resonance; names like exchange, cooperation, and plunder are labels the researcher sticks on, not something written in the books.*

</note>

To merge several [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) into one, or to split one into several [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25), is [recutting](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c22). To claim that the [weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) of a coarse and a fine record agree, one must state how the result asked of the coarse record is obtained from the fine record; where this is not stated, they can only be reported separately and not inferred from one another.

<note>

*Merging several people into one unit, or splitting a unit apart, is recording under a different cut. But whether the weights of a coarse record and a fine record are said to agree must first be explained: how the coarse one is obtained from the fine one.*

</note>

### Properties derived from the model

```text
Conclusion 2-1: Weight reads history; the record of the solution is not a direct input to weight; the record of the solution is the researcher's selection
     (from definition of generating function + Premise 2 + definition of solution)
Conclusion 2-2: History only grows and never shrinks, and is path-dependent; resource variables rise or fall according to actual events, and this paper sets no irreversibility restriction on recorded variables
     (from definition of history + Law of Non-Restoration + Premise 2 + Premise 4)
Conclusion 2-3: Criterion exit is only one dimension of the solution; the criterion judges only the recorded state variables, not the whole history and not the weight
     (from Premise 1 + Conclusion 1-3 + Premise 2 + definition of solution)
Conclusion 2-4: For a wo that is not locked in, the same output does not guarantee the same solution
     (from Law of the Black Box + definition of locked in + definition of weight + Law of Flux + definition of output + definition of solution + Premise 2)
Conclusion 2-5: With many people, the output of other nodes may arrive at this node, enter its history and take part in later weight, so the solution of one node may be affected jointly by its own output and the output of other nodes
     (from Premise 2)
Conclusion 2-6: Membership may change in every derivation round; when it does, the history of each node and the conditions of resonance may change with it
     (from Law of Flux + definition of entry + definition of criterion exit + definition of resonance + Premise 2)
Conclusion 2-7: The model does not require the solution to be optimal, conserved, convergent, or in equilibrium
     (from Premise 1 + Premise 4 + definition of solution)
Conclusion 2-8: The model does not specify the content of responses or the amount of increases and decreases, and therefore cannot predict trajectories
     (from Premise 2 + Premise 4 + definition of solution)
Conclusion 2-9: With many people, on the production dimension, if the outputs of nodes arrive at one another and, within a stated time window, their sequences repeatedly continue one another compatibly, this is resonance; if the conditions of upstream culture are also met, the compatible pattern that resonance leaves behind is the manifestation of culture on the production dimension
     (from Premise 2 + definition of resonance + definition of culture + definition of production)
Conclusion 2-10: Whether the weights of a coarse record and a fine record agree is not guaranteed by the definition of recutting; to claim that they agree, one must state how the result asked by the coarse record is obtained from the fine record, and what is not stated can only be reported separately, without inferring one from the other
     (from definition of recutting + definition of weight)
Conclusion 2-11: If purposes show up as a node's distinction about itself, it enters history and affects later output only through weight; this paper neither confirms nor presupposes the content of purposes
     (from definition of distinction + definition of history + definition of weight + definition of generating function + Premise 2 + unconfirmability of drives)
Conclusion 2-12: The records of this paper cover only physical changes; distinctions enter history and are not within what this paper records
     (from definition of state variable + definition of resource variable + definition of solution + definition of distinction)
```

<note>

*Twelve properties follow directly from the model above. The most important: the same output does not guarantee the same solution, history only grows, and the record is chosen by the researcher.*

</note>

### What this model is not

It does not specify what the response is or how much the increase or decrease is, so it **cannot predict trajectories**; it can only say what is recorded, where one line crosses another, and which properties can be derived. The actual [solution](concepts.md#c05) has to be supplied by a concrete case through data or rules. The level follows the practice of the upstream interaction-dynamics papers: transitions are not specified, and only qualitative properties are derived.

<note>

*This model cannot be used to predict. It only tells you what to record and what properties can be derived. Real numbers must come from concrete cases.*

</note>

---

## 3. Interfaces

```text
Premise 3: A node has state variables and resource variables that can be recorded, and the boundary is specified by the cut;
     what counts as "its own" is the researcher's distinction and does not involve ownership.
Premise 4: The resource variables of a node increase or decrease in every derivation round, determined by actual events.
     This paper does not discuss whether resources "move", nor whether the increases and decreases of different nodes
     correspond to the same portion of something;
     if downstream needs such a correspondence, downstream must specify it itself and bear the distinction of identity involved.
```

<note>

*Two interfaces for downstream: each node has state and resources that can be recorded; resources rise and fall in every derivation round. Whether the rises and falls correspond is for downstream to decide and answer for.*

</note>

---

## 4. The Human Viewpoint (an instance of Premise 1)

From the human viewpoint, the [exit criterion](concepts.md#c04) is often written on physiological state, and some [self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) accordingly marks the [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) with [death](../paper_fb0addca_42b6_4884_aa18_438c695747ab/concepts.md#c22); [criterion exit](concepts.md#c09) follows the criterion the researcher states, and it may or may not coincide with the upstream [exit](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43), and the two do not replace each other; [state variables](concepts.md#c02) are for example stamina, energy reserve, and physiological condition, and [resource variables](concepts.md#c03) are for example food, specified by the researcher. Birth is only [entry](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c44): if the prior history of whoever undergoes [entry](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c44) includes the biological raw material of its parents, that is an overlap of prior history at the time of [entry](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c44) and does not constitute a separate event. The parents' biological raw material is distinct from [learning](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c04): the former is biological constitution, while the latter follows upstream [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) and [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13), and it does not guarantee copying the parents' knowledge, [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06), or [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28).

<note>

*Applying Premise 1 to humans: the exit criterion is often written on physiological state, death is a mark made by some self, and stamina and food are the state and the resource respectively. Birth is just an entry; the bodily material from the parents does not mean the parents' experiences or tendencies are passed to the child as well.*

</note>

```text
Step 4-1: The prior history of an entrant may overlap with that of existing nodes
     (from Premise 2)
Conclusion 4-2: Even if an entrant's prior history overlaps with existing nodes, this does not imply that the entrant inherits the parents' history, weight, or output
     (from Step 4-1 + definition of arrival + definition of history + definition of weight + definition of output + definition of learning)
```

---

## 5. Non-Implication Relations

| No. | Content | Derivation |
|---|---|---|
| 5-1 | the [criterion](concepts.md#c04) is a definition, and whether [criterion exit](concepts.md#c09) occurs is a fact | Conclusion 1-3 |
| 5-2 | when not [locked in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12), an inference about [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) cannot replace the judgment of whether [criterion exit](concepts.md#c09) occurs | see below |
| 5-3 | an [entry](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c44) that has occurred is not cancelled by a later [exit](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43) | see below |
| 5-4 | the source attribution of a [resource variable](concepts.md#c03) does not affect how [criterion exit](concepts.md#c09) is judged | see below |
| 5-5 | variation and screening do not imply improvement, optimality, or sustainability | see below |
| 5-6 | an increase or decrease of a [resource variable](concepts.md#c03) does not imply which kind of [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) it belongs to | see below |
| 5-7 | the boundary between [resource](concepts.md#c03) and [state](concepts.md#c02) is a [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23), and different [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) can each record variables related to the same object | see below |

<note>

*This chapter lists seven "cannot be inferred this way" cases: each comes with a derivation showing why A does not give B.*

</note>

### 5-2 Weight inference cannot replace judgment

The "not [locked in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12)" in this section and in 5-5 follows the upstream definition of [lock-in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12).

<note>

*"Not locked in" is what upstream says: at the next moment there is more than one possible outcome.*

</note>

```text
Step 5-2-1: An inference by a self about the weight of a wo (upstream calls it impression) concerns how heavy the branches are,
     while a judgment concerns an event that has already occurred
     (from definition of weight + definition of impression + Step 1-2)
Step 5-2-2: For a wo that is not locked in, weight yields only a tendency and not a result
     (from Law of the Black Box + definition of locked in + definition of weight)
Conclusion 5-2-3: For a wo that is not locked in, an inference about weight cannot give a complete next moment; whether criterion exit occurs must still be judged from actual events and cannot be replaced by an inference about weight
     (from Step 1-2 + Step 5-2-1 + Step 5-2-2)
```

### 5-3 An entry that has occurred is not cancelled

```text
Step 5-3-1: Exit does not delete the positions in history that have already occurred
     (from definition of history)
Step 5-3-2: Whether something is a wo is determined by actual history; entry is starting to be a wo (including being a wo from the first moment),
     exit is ceasing to be a wo; every exit comes after its corresponding entry
     (from definition of history + definition of wo + definition of entry + definition of exit)
Step 5-3-3: For a wo that has exited, its own segment remains in history
     (from definition of exit + definition of history + definition of own segment)
Conclusion 5-3-4: Exit does not cancel an entry that has occurred, nor the arrivals left by the entrant
     (from Step 5-3-1 + Step 5-3-2 + Step 5-3-3 + definition of arrival)
Remark: Criterion exit is the researcher's record and does not change history.
```

**Calibration.** A person has a child and then [exits](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43): the child's [entry](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c44) and his own [exit](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43) each occupy a place in the [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13), and the latter does not cancel the former. This is a calibration, not a source of derivation.

<note>

*An entry that has already happened does not become something that never happened because someone exits later.*

</note>

### 5-4 The source attribution of a resource variable does not affect how judgment is made

```text
Step 5-4-1: If a node's resource variable increases in a derivation round, the increase is a record of an actual event
     (from Premise 4)
Step 5-4-2: Whether to record this increase as related to the output of other nodes is the researcher's attribution
     (from definition of distinction + definition of output + Premise 4)
Step 5-4-3: Within one cut, whether a node undergoes criterion exit in a given derivation round depends only on the state variables and the criterion,
     and not on the source attribution of the resource variables
     (from Premise 1 + Step 1-2)
Conclusion 5-4-4: How an increase of a resource variable is attributed does not change how criterion exit is judged;
     this paper does not infer from it any relation between resources and states
     (from Step 5-4-2 + Step 5-4-3)
Remark: This does not imply that other nodes intended to give, nor that the total resources of the whole network increased;
     the former is bound by the unconfirmability of drives, and the latter must be accounted for separately within one cut.
```

**Calibration.** A poor man eats one bowl of rice a day, while a rich man can eat steak, turkey, and all kinds of burgers; their [resource variables](concepts.md#c03) are very different, but there is no reason that the poor man must [criterion exit](concepts.md#c09) at once. Conversely, a rich man who is hit by a car on the road does not die because of desire, nor because of insufficient resources. [Criterion exit](concepts.md#c09) has no necessary relation to what one wants or how many resources one has; it is only a limit given by reality. This is a calibration, not a source of derivation.

<note>

*How a resource increase came about is the researcher's account; deciding whether someone undergoes criterion exit looks only at the state and the criterion, and has nothing to do with where the resource came from.*

</note>

### 5-5 Variation and screening do not imply improvement, optimality, or sustainability

```text
Step 5-5-1: For a wo that is not locked in, the weight determined by history yields only a tendency, and the result can vary
     (from Law of the Black Box + definition of locked in + definition of weight)
Step 5-5-2: This paper does not require the exit criterion to contain any "better" condition;
     the criterion is only a condition that the researcher specifies, expressed in the state variables
     (from Premise 1)
Step 5-5-3: Whether the criterion is satisfied and the changes of resource variables are recorded separately,
     and this paper does not infer a relation between the two
     (from Premise 1 + Premise 2)
Conclusion 5-5-4: Output variation and criterion exit records are not enough to infer monotonic improvement, optimality, or sustainability of state or resources
     (from Step 5-5-1 + Step 5-5-2 + Step 5-5-3 + Conclusion 2-2 + definition of output + definition of exit criterion + definition of criterion exit)
Remark: "Screening" is only a metaphor upstream, and it must be marked as a metaphor.
```

### 5-6 Records of increases and decreases do not imply the type of output

```text
Step 5-6-1: The increases and decreases of resource variables are the separate records of the nodes concerned, determined by actual events
     (from Premise 4)
Step 5-6-2: After output arrives at the other party, it enters the other party's history
     (from Premise 2 + definition of output + definition of arrival + definition of history)
Conclusion 5-6-3: If resource variables record only numerical increases and decreases and carry no information about output types, then the books of resource variables record only numerical increases and decreases; an output type is a classification mark that the researcher adds separately,
     outside what the books of resource variables record, so it cannot be read off those books
     (from Premise 1 + Step 5-6-1 + Step 5-6-2 + definition of solution)
Remark: Motives, needs, and purposes are bound by the unconfirmability of drives and cannot be read off the books;
     this concerns motives only and is not taken as sufficient proof that output types are unidentifiable.
Remark: Violence, exchange, gifts, deception, and the like are the researcher's names for output and are not components of the model.
Remark: This paper presupposes no antecedent about ownership or legitimacy.
```

**Calibration.** Violence, bribery, intimidation, and fraud, seen on the books, are all just that some person's [resource variable](concepts.md#c03) decreases and another's increases; what distinguishes them lies in the content of the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) and the [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10), named by the researcher. This is a calibration, not a source of derivation.

<note>

*The books only show "who has more and who has less"; they cannot tell whether it was robbery, fraud, or a gift. That is a name the researcher sticks on afterwards.*

</note>

### 5-7 The boundary between resource and state is a cut

```text
Step 5-7-1: The researcher may, under cuts with different nodes and different viewpoints,
     each record the resource variables or state variables related to some object
     (from Conclusion 1-5 + Premise 3; for example the researcher may, in the accounts of a resident and of the national territory as two nodes,
      each record variables related to one house; a worker's labour is recorded as a state for the worker and as a resource for the boss)
Conclusion 5-7-2: Variables related to the same object appearing in the accounts of different nodes do not constitute a contradiction,
     nor do they imply who owns it, nor presuppose that these variables refer to the same thing
     (from Step 5-7-1 + Premise 3 + definition of identity)
Step 5-7-3: Under each cut, the category of a variable is fixed by that cut, and this paper does not presuppose that variables across cuts refer to the same thing; if variables related to the same object were added directly, double counting would be only one consequence
     (from Conclusion 1-5 + Conclusion 5-7-2)
Conclusion 5-7-4: This paper does not claim that resources 'move' between nodes; it records only the increases and decreases of each node's resource variables
     (from Premise 4)
Remark: This paper does not add resource variables and state variables across cuts (item 3 of the prohibition list); totals are accounted only within one cut; conclusions under different cuts are not directly inferred from one another.
Remark: Movement presupposes identity, and identity is a distinction; this is the reason this paper does not discuss movement, not a source of derivation.
Remark: Under the cut exemplified in this paper, a worker is a node, not a resource; what is recorded under the boss's cut is the resource variable corresponding to the worker's labour (output).
```

**Calibration.** A piece of land can be both private and the national territory; the accounts on each side keep their own way of recording, and there is no conflict. This is a calibration, not a source of derivation.

<note>

*For the same piece of land, the resident's books and the nation's books each record their own; they do not conflict, but the numbers on the two sides cannot simply be added together.*

</note>

---

## 6. Cases (Calibration)

This whole chapter is only calibration and is not a source of any derivation. The two cases are well-known historical situations, and each case has two parts: first the course of events is told in full, without model language; then, following that course, the same notation is walked through once, changing only the [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25), the variables, and the [criterion](concepts.md#c04). The data are taken from public web pages, not from original files; where a source attributes something to the same event, that is marked as the source's attribution and is not read off the books.

<note>

*The two cases are only demonstrations: first tell the story to the end, then walk through it again with this paper's notation.*

</note>

### 6.1 The buffalo jump hunting grounds of the North American Great Plains

**Narrative.** On the North American Great Plains, people drove whole herds of buffalo off cliffs and butchered them at the bottom. Head-Smashed-In in Alberta, Canada, is one of the largest and oldest of them: the cliff is about ten to eighteen metres high, and it was used from about 5,700 years ago until the middle of the nineteenth century. The top of the cliff is lush pasture, and starting ten kilometres west of the cliff, more than five hundred stone cairns are arranged in drive lines; people lit fires or waved blankets beside the cairns to lead the buffalo toward the edge.

A drive was not the work of a single day. The cairns had to be built first and were repaired over the years; each hunting ground also had its own rituals, for example the Blackfoot believed that sea fossils could attract buffalo, and they had a place in the rituals before hunting. On the day itself, a herd was selected first, and a hunter put on a calf's hide and imitated the frightened cry of a calf to lead the herd into the drive lines; others might wear wolf hides and press from behind; scarecrows stood on the cairns or branches were piled on them, and the people beside the cairns waved hides to make the herd run faster and faster. Hunting grounds were often chosen where the terrain undulates, so that the cliff edge was hidden and the buffalo could not turn back when they saw it. The man in the calf hide had to step aside at the last moment, but there are many records of him falling to his death.

After the buffalo fell, people processed them at the camp below the bottom of the cliff: the bones at the bottom are in some places eleven metres deep, the camp stretches about one kilometre, with meat cellars and cooking pits left behind, and a layer of broken bone nearly one metre thick lies underground. Archaeology in Montana has also found pits for heating fat and for roasting, used to make meat into a cake mixed with fat and berries (pemmican); the pemmican and the hides were both trade goods. The hunting ground ceased to be used after the middle of the nineteenth century, when the buffalo were almost extinct.

<note>

*First the story: the herd is led into the corridor formed by cairns, the man in calf hide leads the way by imitating calls, people on both sides wave hides to drive the buffalo, the buffalo rush off the cliff, and the people at the bottom process the meat and hides. The decoy sometimes fell to his death with them.*

</note>

**Bringing it into the framework.** First choose the [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23): the [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) are the decoy, the people beside the cairns, the people driving from behind, the people processing at the bottom, and the people who built and repaired the cairns earlier; the herd and the terrain stay in the [natural environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16), and this paper does not ask about the inside of the herd.

First look at a [derivation round](concepts.md#c07) on the day of a drive. The decoy puts on the hide and imitates the cry; that is [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28), and it [arrives](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the herd; the herd moves toward him, which is a response of the [natural environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16); this movement [arrives](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the decoy and also [arrives](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the people beside the cairns, and becomes part of each one's [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13). The people beside the cairns, based on this [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13), produce the next [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28), waving hides, and the movement of the herd changes once more. The [solution](concepts.md#c05) of this [derivation round](concepts.md#c07) is each person's [state variables](concepts.md#c02) and [resource variables](concepts.md#c03) at the next moment: the decoy is still on the line, and the people at the bottom do not yet have meat in their hands; the herd being closer to the cliff is a change in the [natural environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16) and is not in the [solution](concepts.md#c05).

Now look at the last [derivation round](concepts.md#c07). The decoy steps aside or falls off the cliff; this is the result reality gives: if the [exit criterion](concepts.md#c04) takes a specified threshold of physiological state (those who fall are often marked as [death](../paper_fb0addca_42b6_4884_aa18_438c695747ab/concepts.md#c22)), the one who falls is recorded as [criterion exit](concepts.md#c09) in this [derivation round](concepts.md#c07). His [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) remains, and the herd and companions that his [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) has already [arrived at](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) are not cancelled because of this (5-3). In the [derivation round](concepts.md#c07) at the bottom of the cliff, the [resource variables](concepts.md#c03) of meat and hide for each person increase, and how much they increase is determined by the event of the time; the next time the herd may not enter the line, and the same [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) does not guarantee the same [solution](concepts.md#c05) (Conclusion 2-4).

Finally, stretch the time. The people who built the cairns left their [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) on the ground, and generation after generation it [arrived](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at later people and became part of their [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13); earlier [derivation rounds](concepts.md#c07) thus affect later [solutions](concepts.md#c05). The [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of the [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) [arrive](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at one another and, within this time window, repeatedly continue in a compatible way on the [production](concepts.md#c01) dimension, which is what upstream calls [resonance](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c09), and the compatible pattern it leaves can be seen as the manifestation of [culture](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c13) on the [production](concepts.md#c01) dimension. That the researcher calls this continuation cooperation or division of labour is naming and cannot be read off the books.

<note>

*Walking through it again with this paper's notation: first decide who counts as a node, and leave the herd in the environment; in one derivation round the luring, the herd's reaction, and the others waving hides link one into the next; in the last derivation round someone falls and is recorded as criterion exit; in the derivation round at the bottom, everyone's meat and hides increase; over the long run, the output of the cairn builders is left behind generation after generation, everyone's actions keep fitting together, upstream calls it resonance, and calling it cooperation is sticking on a label.*

</note>

**What can be said and what cannot be inferred.** What can be said: in these sources' accounts, the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of many people continues in sequence, with some falling to their deaths and undergoing [criterion exit](concepts.md#c09) because of it, and others obtaining meat and hides because of it. What cannot be inferred: how much each person gets, who commands whom, and each person's motives. Some sources attribute this arrangement to the leadership of societies and rituals, and others say "it differs from place to place"; these are the sources' attributions, and it also cannot be inferred that every drive went the same way.

**Data notes.** The details of the process (calf hide, wolf hide, sea fossils, pemmican) come mainly from an archaeological report on the Blackfoot hunting grounds of Montana, which says that places differ and that this is roughly how it went; the Head-Smashed-In sources are used only to supply dates, scale, the camp at the bottom of the cliff, and the bone layer. How the meat was divided and distributed after the hunt, the specific division of labour of each society, and data on the stamina or casualties of individual participants were not found and are not written.

<note>

*All that can be said is "some people took turns repeatedly, some fell to their deaths, some obtained meat and hides"; how much each person got, who commanded whom, and why, the sources do not make clear, so they are not written. Most details come from the Montana report, not from this site itself.*

</note>

### 6.2 Ford's moving assembly line at Highland Park

**Narrative.** Ford Motor Company, founded in 1903, reached twenty-five cars a day within two years, but at that time building a car was stationary: the chassis stayed in place, workers brought parts to it and did a little of everything, and the fastest record was twelve hours and thirteen minutes.

In April 1913, the flywheel magneto was the first to go on a moving line. Where one worker used to finish one in about twenty minutes, the operations were now broken apart so that each person installed only one part and passed it to the next; output first shrank to thirteen minutes and then to five. Then the engine and the transmission also went onto the line; between summer and autumn of the same year, the chassis too was pulled across the work area with ropes and a winch, workers installed parts along the line, a car dropped to five or six hours, in 1914 it shrank to ninety-three minutes, and about a thousand could be produced a day. Car prices fell, which Ford called a dream of making cars affordable for the middle class.

The cost fell on the workers. Many skilled workers found the work monotonous and exhausting; most of the factory consisted of new immigrants, of at least fifty-three nationalities speaking more than a hundred languages, and the company required them to attend English school. At the end of 1913 the turnover rate reached 380 percent, and to add one hundred workers the company had to hire 963. In January 1914 the company announced five dollars for eight hours, up from 2.34 dollars for nine hours, and said it would share part of the profits with workers. In practice, this wage was given only to people whose private lives had been investigated and who were deemed "qualified". The work did not become easier and the pace did not slow, but the next morning ten thousand people gathered at the factory gate waiting to apply.

<note>

*First the story: formerly one person handled a whole car, taking a dozen-odd hours; after changing to each person doing one small motion with the car moving along the line, a car took only an hour and a half. But the workers could not bear it, and turnover in a year was over three times; so the company raised the daily wage to five dollars, and the next day tens of thousands queued.*

</note>

**Bringing it into the framework.** First choose the [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23): the workers at each station and the company are [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25); the machines, conveyors, and parts stay in the [natural environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16), and this paper does not ask about their inside.

First look at one [derivation round](concepts.md#c07) on the magneto line. A worker installs a part; that is [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28); the movement of the conveyor and of the semi-finished work is a response of the [natural environment](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c16), which [arrives](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the next worker and becomes part of his [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13), and on this basis he produces his next motion. The [solution](concepts.md#c05) of this [derivation round](concepts.md#c07) is each person's number of parts handled, stamina, and whether he is still at his post. The reason this string of [outputs](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) could be linked into a line is that each person's [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) happens to enter the next person's next moment; the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) sequences of the workers repeatedly continue in a compatible way within a shift, which is what upstream calls [resonance](../paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3/concepts.md#c09), and the researcher calls it cooperative [production](concepts.md#c01), which is naming.

Now look at quitting. The [exit criterion](concepts.md#c04) takes quitting, as specified by the author of this paper: 380 percent is a fact under this [criterion](concepts.md#c04), the [criterion](concepts.md#c04) itself is a definition, and the two do not replace each other (Conclusion 1-3). Quitting is one dimension of the [solution](concepts.md#c05) of some [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) in a given [derivation round](concepts.md#c07), and has no necessary relation to whether they want anything or how many resources they have; whether a person leaves at the next moment can still be inferred from [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) only as a tendency and not as a result.

Wages and labour are variables recorded separately by two [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25): labour is a [state variable](concepts.md#c02) for the worker (stamina and mood) and a [resource variable](concepts.md#c03) for the company; the increases and decreases of wages are booked separately (5-7). The announcement of five dollars is a new [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10); it enters many people's [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) and changes their [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) at the next moment; ten thousand people arriving at the factory gate is the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) that some of these [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) produce in the next [derivation round](concepts.md#c07).

<note>

*Walking through it again with this paper's notation: the workers and the company are nodes, and the machines and parts stay in the environment; in one derivation round, one person installs a part, the car moves to the next station, and the next person carries on; the turnover rate is a fact after we specify "quitting counts as criterion exit"; wages and labour sit in different books; the announcement of a raise is a new arrival.*

</note>

**What can be said and what cannot be inferred.** What can be said: after the assembly line was introduced, turnover was very high, which is one fact; after the raise was announced, a large crowd gathered at the factory gate, which is another fact. What cannot be inferred are motives and relations: the public television page writes the raise as an effort to lower turnover, and the Ford museum writes it as workers accepting assembly-line discipline in exchange for high pay; these are the sources' own attributions and, under the [unconfirmability of drives](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c03), cannot be read off the books; nor can it be inferred that the raise reduced turnover, because this paper does not presuppose a relation between [resources](concepts.md#c03) and [criterion exit](concepts.md#c09), and that is an empirical question for downstream.

**Data notes.** The date on which the chassis went onto the line is stated differently by different sources (August or October 7), and the text says "between summer and autumn". "Sharing part of the profits with workers" appears only on the public television page. Individual workers' fatigue, what the wages were used for, and the actual motions at each station were not found and are not written.

<note>

*What can be said is that turnover was high and a crowd came after the raise; as for whether the raise was to retain workers or workers traded discipline for high pay, the two sources each say their own thing, and we do not draw a conclusion for them.*

</note>

**Sources**

- Head-Smashed-In Buffalo Jump (Parks Canada)
- The Buffalo Chasers (Archaeology magazine)
- Working at Ford's Factory (PBS American Experience)
- Crowd of Applicants outside Highland Park Plant (The Henry Ford)
- Ford Methods and the Ford Shops (The Henry Ford)
- Moving Assembly Line at Ford (History.com)
- Ford material reprinted by Plant Engineering

<note>

*Source URLs:*

*https://www.parks.canada.ca/culture/spm-whs/sites-canada/sec02e*

*https://archaeology.org/issues/online/collection/letter-from-montana-buffalo-jumps/*

*https://www.pbs.org/wgbh/americanexperience/features/-gallery-henryford*

*https://www.thehenryford.org/collections-and-research/digital-collections/artifact/35765*

*https://thehenryford.org/explore/blog/ford-methods-and-the-ford-shops*

*https://history.com/this-day-in-history/moving-assembly-line-at-ford*

*https://www.plantengineering.com/?p=17048*

</note>

---

## 7. Empirical Antecedents and Conditions of Failure

**Empirical antecedents:** This paper contains no empirical antecedent; any relation among [resources](concepts.md#c03), [states](concepts.md#c02), and [criterion exit](concepts.md#c09) is raised and borne by downstream research.

**Calibration and cases:** The calibrations in the sections and the cases in Chapter 6 are not used to support any conclusion of this paper. The data of the two cases are only public web pages, making the model look close to reality; they do not mean the model has been validated.

<note>

*To close: this paper contains no empirical antecedent, and the cases are only for comparison and do not count as validation.*

</note>

**Conditions of failure**

- When Premise 1 to Premise 4 are negated, the conclusions whose source chain contains the negated premise lose their source; losing the source only means that the derivation chain is broken, not that the conclusions are false. Premise 1 is a scope setting, and Premise 2 to Premise 4 are settings of the [derivation round](concepts.md#c07) structure and the interfaces; none of them is a claim that can be refuted, and negating them means switching to another set of settings.
- The derivations of this paper depend on the upstream [Law of the Black Box](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c25), [Law of Flux](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c24), and [Law of Non-Restoration](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c28), and on the definitions of [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13), [generating function](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c15), [impression](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c45), [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23), [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18), and [identity](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c21); whether they hold is left to the upstream papers, and this paper sets no separate failure condition for them.
- If downstream introduces a relation between [resources](concepts.md#c03) and [criterion exit](concepts.md#c09), or a correspondence between the increases and decreases of different [nodes](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25), that relation is downstream's empirical antecedent, or a [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) made about [identity](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c21), and its failure does not affect this paper.

<note>

*Under what conditions which derivations lose their ground: changing Premise 1 to Premise 4 means changing the scope and the model; whether an upstream law or definition holds is left to the upstream papers; relations downstream adds itself, if they fail, do not affect this paper.*

</note>

**Not conclusions of this paper:** Topics such as division of labour, distribution, continuation of knowledge, care, and support are only the researcher's names for compatible repeated continuation on the [production](concepts.md#c01) dimension, and are left to sub-papers. Data that were not obtained (post-hunt distribution, the division of labour of each society, individual workers' fatigue and the use of wages) are not written as "nonexistent" and are not used to support conclusions.

<note>

*Division of labour, distribution, and care are not handled in this paper and are left to sub-papers; data not found is not treated as "nothing".*

</note>
