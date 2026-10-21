---
uuid: 2ccbf179-58cf-4851-bd72-5fa886e4b2c3
last-modified: 2026-10-17T02:05:51
---

<a id="c01"></a>
## public 單元 / Unit

- 定義：在一次[切法](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23)下，兩個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)、各自的[小迴路](concepts.md#c02)，以及把兩者連起來的[大迴路](concepts.md#c03)所構成的結構。單元必須講明在哪個維度上判斷。[信號容器](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26)與[隨機源](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c27)可以接在單元的[邊](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29)上，但不是單元的主體。
- Definition: The structure formed, under one [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23), by two [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), the [small loop](concepts.md#c02) of each, and the [large loop](concepts.md#c03) connecting them. A unit must state the dimension in which it is judged. A [signal container](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26) or a [random source](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c27) may attach to an [edge](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29) of the unit but is not its main body.

<a id="c02"></a>
## public 小迴路 / Small Loop

- 定義：一個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)的[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)回到自己的[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)、參與自己之後的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)的[迴路](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c31)。只有[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)有小迴路；[信號容器](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26)與[隨機源](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c27)沒有。
- Definition: The [loop](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c31) in which the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) returns to its own [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) and takes part in its later [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06). Only a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) has a small loop; a [signal container](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26) or a [random source](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c27) does not.

<a id="c03"></a>
## public 大迴路 / Large Loop

- 定義：連接兩個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)的[迴路](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c31)：甲的[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)乙，乙的[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)甲，各自參與對方之後的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)。
- Definition: The [loop](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c31) connecting two [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16): the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of A has [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at B, the output of B has arrival at A, and each takes part in the other's later [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06).

<a id="c04"></a>
## public 學習 / Learning

- 定義：[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)的材料累積進[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)、可能改變後續[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)的過程。[學習](concepts.md#c04)是結果條件，不指目的，沒有預設方向，不保證學到，也不保證學對。判定只看[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)之後可觀察的[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)有改變，不證明改變來自哪一筆[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)。
- Definition: The process by which material that has had [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) accumulates in [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) and may change later [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06). [Learning](concepts.md#c04) is a result condition, not a purpose; it has no preset direction and guarantees neither that something is learned nor that it is learned correctly. It is judged only by an observable change in [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) after [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10), and does not show which [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) caused the change.

<a id="c05"></a>
## public 結果回饋 / Outcome Feedback

- 定義：一個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)之後的[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)再[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)自己的後續互動，帶回實際後果。事前[學習](concepts.md#c04)的方向不可預測，事後的結果可以判；判準在其他[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)與[自然環境](concepts.md#c16)的反應結果裡，不在發生[學習](concepts.md#c04)的[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)內部。
- Definition: The actual consequence brought back when the later [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) of a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) has [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) in its own subsequent interactions. The direction of [learning](concepts.md#c04) cannot be predicted beforehand, while the result can be judged afterwards; the criterion lies in the responses of other [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) and the [natural environment](concepts.md#c16), not inside the [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) in which [learning](concepts.md#c04) occurs.

<a id="c06"></a>
## public 退出成本 / Exit Cost

- 定義：一個[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)讓一條[邊](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29)上的[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)停止或減少時，要承受的[結果回饋](concepts.md#c05)。每一端各算各的，可以不對稱。成本低，就是可以輕易放棄這條[邊](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29)；成本高，就是幾乎不得不繼續。退出成本的層級是[邊](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29)，與[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)層級的[退場](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43)不同，兩者不互相蘊含。
- Definition: The [outcome feedback](concepts.md#c05) that a [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) must bear when it stops or reduces [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) on an [edge](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29). Each end counts its own, and the two may be asymmetric. Low cost means the edge can be given up easily; high cost means one can hardly help continuing. Exit cost is at the level of the [edge](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29), unlike [exit](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43) at the level of the [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25), and neither implies the other.

<a id="c07"></a>
## public 可達子圖 / Reachable Subgraph

- 定義：以某個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)為中心，在講明的時窗內，所有曾有[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)它的[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)，連同這些[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)與它之間的[邊](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29)所構成的子圖。只計已[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)者。每個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)各有一張。
- Definition: The subgraph centered on a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), within a stated time window, made of all [presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) whose [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) has had [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at it, together with the [edge](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29) between each of them and it. Only what has already had arrival counts. Each [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) has its own.

<a id="c08"></a>
## public 大型節點 / Large Node

- 定義：在講明的[切法](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23)與時窗內，[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)的[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)數極大、幾乎涵蓋所指範圍內每一個[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)的[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)，例如政府、公眾人物、大型企業。它是真實存在的[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)；[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)廣不蘊含影響大，不蘊含全知，也不蘊含有目的。
- Definition: A [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) whose [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) has [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at an extremely large number of nodes, almost every [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) in the stated range, within a stated [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23) and time window; for example a government, a public figure or a large enterprise. It is a real node. Wide [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) does not imply large influence, omniscience or purpose.

<a id="c09"></a>
## public 共振 / Resonance

- 定義：在講明的維度與時窗內，多個[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)的[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)模式反覆相容接續。[共振](concepts.md#c09)須以[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)為條件：[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)彼此之間有[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)，或有共同的來源（例如[信號容器](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26)、[自然環境](concepts.md#c16)）對它們都有[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)；完全沒有[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)，就沒有[共振](concepts.md#c09)。判準是相容，也就是[失配](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c08)的反面，不是距離；因此不等於[趨同](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c37)。是否[共振](concepts.md#c09)，只能事後說。[共振](concepts.md#c09)不同於[默契](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c06)：[默契](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c06)是兩個[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)之間按維度計的[專屬歷史](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c05)上的相容接續，[共振](concepts.md#c09)不要求[專屬歷史](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c05)，也不限兩個[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)。
- Definition: Within a stated dimension and time window, the repeated compatible succession of the [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) patterns of several [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25). [Resonance](concepts.md#c09) requires [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10): either arrival between the [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)s, or arrival at all of them from a common source (such as a [signal container](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26) or the [natural environment](concepts.md#c16)). Without any arrival, there is no [resonance](concepts.md#c09). The criterion is compatibility, the opposite of [mismatch](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c08), not distance; hence it is not [convergence](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c37). Whether [resonance](concepts.md#c09) holds can only be said afterwards. [Resonance](concepts.md#c09) differs from [tacit coordination](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c06), which is compatible succession on the [dedicated history](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c05) between two [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) counted per dimension; [resonance](concepts.md#c09) does not require [dedicated history](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c05) and is not limited to two [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19).

<a id="c10"></a>
## public 懲罰 / Penalty

- 定義：一個[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)之後，[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)輸出者的、在講明的判準下算作代價的[結果回饋](concepts.md#c05)。判準須講明是誰的：輸出者自己的[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)，或研究者的判準，兩者可以不一致。懲罰不一定是暴力，不一定被明列。
- Definition: A [outcome feedback](concepts.md#c05) that has [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the producer after an [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) and counts as a cost under a stated criterion. The criterion must say whose it is: the producer's own [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) or the researcher's criterion, and the two may disagree. A penalty need not be violent or explicitly listed.

<a id="c11"></a>
## public 強制 / Enforcement

- 定義：已被明列成規則、寫在[信號容器](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26)裡、違反可由第三方判定的[懲罰](concepts.md#c10)。[懲罰](concepts.md#c10)可以是暴力，也可以不是。[強制](concepts.md#c11)不保證遵守，也不保證違反被判定與執行；規則先於違反，是另一個時間點的事。
- Definition: A [penalty](concepts.md#c10) that has been explicitly listed as a rule, written in a [signal container](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26), with violation judgeable by a third party. The penalty may or may not be violent. [Enforcement](concepts.md#c11) guarantees neither compliance nor that violations are judged and carried out; the rule precedes the violation, at another point in time.

<a id="c12"></a>
## public 容錯率 / Fault Tolerance

- 定義：在講明的維度上，一個[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)對對方的[失配](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c08)所能容忍的程度的上限；超過上限，這條[邊](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29)就可能終止。
- Definition: The upper limit, in a stated dimension, of the degree of [mismatch](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c08) by the other side that a [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) can tolerate; beyond it the [edge](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c29) may terminate.

<a id="c13"></a>
## public 文化 / Culture

- 定義：研究者對網路中出現的[共振](concepts.md#c09)的命名。在指定的[切法](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23)、維度與時窗下，它是無數[單元](concepts.md#c01)在特定[自然環境](concepts.md#c16)約束下，經過反覆的[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)、[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)與[結果回饋](concepts.md#c05)，留在[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)中的一組相容圖樣：哪些維度組合能不被反覆打斷地接續下去。文化不以是否寫下來為界，沒有主體，不被某人持有或傳遞，也沒有尺度。
- Definition: A researcher's name for a [resonance](concepts.md#c09) appearing in the network. Under a specified [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23), dimension and time window, it is a set of compatible patterns left in [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) by countless [unit](concepts.md#c01) through repeated [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28), [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) and [outcome feedback](concepts.md#c05) under particular [natural environment](concepts.md#c16) constraints: which combinations of dimensions can continue without being repeatedly interrupted. Culture is not bounded by whether it is written down, has no subject, is not held or transmitted by anyone, and has no scale.

<a id="c14"></a>
## public 模板 / Template

- 定義：抽象化、可重複使用的規則或資料，被明列在[信號容器](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26)裡。[容器](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26)被[鎖死](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12)，內容不會自己變，在某維度上提供固定點，不需要先[共振](concepts.md#c09)就能取得。同一規則有兩份文本就是兩份模板。模板是[容器](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26)裡的固定內容，不是[文化](concepts.md#c13)的一層。模板的有無依研究者指定的判準判定。
- Definition: Abstracted, reusable rules or data explicitly listed in a [signal container](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26). The [container](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26) is [lock-in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12), so the content does not change by itself and provides a fixed point in some dimension, obtainable without first having [resonance](concepts.md#c09). Two texts of the same rule are two templates. A template is fixed content in a [container](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c26), not a layer of [culture](concepts.md#c13). Whether a template exists is judged by a criterion the researcher specifies.

<a id="c15"></a>
## public 崩潰 / Collapse

- 定義：在指定尺度、維度與時窗下，原來的秩序失去了；秩序指不必逐次指導就能相容接續的預設與接續模式。事後的[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)，由作判斷的[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)劃定。崩潰有程度、逐維度，沒有尺度。崩潰與[失配](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c08)、[退場](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43)判準不同，可為成因或形態，不互相蘊含。
- Definition: Under a specified scale, dimension and time window, the loss of the former order; order means defaults and patterns of succession that are compatible without being directed each time. It is an after-the-fact [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18), drawn by the [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) who makes the judgment. Collapse comes in degrees and per dimension, with no scale. Collapse differs in criterion from [mismatch](../paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e/concepts.md#c08) and [exit](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c43); it can be a cause or a form of them, and neither implies the other.

<a id="c16"></a>
## public 自然環境 / Natural Environment

- 定義：在講明的[切法](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23)下，不屬於模型中任何[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)、卻有[輸出](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28)[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)這些[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)的部分，包含物理條件，例如地形、氣候、水與食物。它對各[節點](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25)最基本的行為構成約束。
- Definition: The part which, under a stated [cut](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c23), belongs to no [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25) in the model yet whose [output](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c28) has [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at these [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25), including physical conditions such as terrain, climate, water and food. It constrains the most basic behavior of each [node](../paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6/concepts.md#c25).

<a id="c17"></a>
## public 落空比例 / Proportion of Failed Expectations

- 定義：在指定的維度與時窗下，事後統計的、原本相容的接續中未能接上的比例。它是外顯統計，不是真實[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)的讀數。
- Definition: The after-the-fact statistical proportion, under a specified dimension and time window, of originally compatible successions that failed to connect. It is an outward statistic, not a reading of true [weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06).

<a id="c18"></a>
## private 外顯資料 / Outward Data

- 定義：被選中、被表達出來的結果及其摘要。外顯資料不等於完整的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)與依賴路徑。
- Definition: The results that are selected and expressed, and their summaries. Outward data are not equal to the complete [weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) and dependence paths.

## 不登記

- 名聲：2026-10-17 作者裁決：日常用語。
- 禮節：2026-10-17 作者裁決：日常用語。
- 時窗：2026-10-17 切法的組成之一，已由上游「切法」涵蓋；本篇未另行定義。
- 維度：2026-10-17 切法的組成之一，已由上游「切法」涵蓋；本篇未另行定義。
- 約束級結論、條件級結論：2026-10-19 作者裁決：論文自身的結論分級標籤，不是模型中的構件；上游概念庫亦未登記同類標籤。
- 陌生人：2026-10-17 日常用語；正文第〇節明說它不是模型裡的量。2026-10-20 作者裁決：本篇的用語約定，本篇所說的熟人與陌生人只指其輸出曾到達該我者；這是範圍上的約定，不由可達子圖的定義推出，不另立概念。
- 熟人：2026-10-17 日常用語；正文第〇節明說它不是模型裡的量。2026-10-20 作者裁決：同「陌生人」的用語約定。
- 陌生：2026-10-17 日常用語，指「陌生人」所處的情形；正文第〇節明說它不是模型裡的量。
- 和平學習：2026-10-20 作者裁決：一般用語，不指向「學習」概念，故正文第七節不加連結。
- 陌生人的起點：2026-10-19 作者裁決：不另立概念。它由專屬歷史的定義直接給出（某維度上第一次到達的那一刻，專屬歷史在該維度上從零開始），論文第四節表格的一行只是對此的說明。
- 劃群：2026-10-17 正文明說是一次分別，不另立概念。
- 內生／外生、不強制／強制的兩個切分：2026-10-17 正文自稱是關係，不是世界上有哪幾種東西。
- 荷包蛋、海膽：2026-10-17 比喻，正文自稱不是模型的一部分。
- 全域圖：2026-10-17 只在 2.5 節作為尺度之一被提及，是切法下的一個範圍，未獨立定義。
- 經驗前件、範圍設定、生物人前件：2026-10-19 作者裁決：前提與第四節的分類標籤，屬論文結構，不是模型構件。
- 自然環境約束的強耦合與不可拼裝：2026-10-17 一條條件級結論的標題，不是構件。
- 經驗：2026-10-20 作者裁決：本篇的用語約定，指已經到達的輸出所構成的歷史（歷史的一個限定），不另立概念；正文第〇節已明說。
- 認識：2026-10-20 作者裁決：本篇的用語約定，只指對方的輸出到達過，是既有「輸出」「到達」的用法，不另立概念；正文 2.3 節已明說。
- 一輪更新：2026-10-20 作者裁決：2.2 節對輸出、到達、學習、結果回饋四步驟的稱呼，由既有概念組成，不是新構件，不另立概念。
- 秩序：2026-10-20 作者裁決：崩潰定義內的說明用語（不必逐次指導就能相容接續的預設與接續模式），隨崩潰一併定義，不另立概念。
- 觀察者：2026-10-17 本篇已改用「研究者」，「研究者」為日常用語；上游公開概念「觀察者」本篇不引用。

## 待決項

無
