---
uuid: b9df3b68-23ee-4839-965b-ed0e6b283ee6
last-modified: 2026-10-05T18:39:00
---

<a id="c01"></a>
## public 不會發生 / Never Occurs

- 定義：不會發生是禁止的一種形式：它說某件事不會發生；被禁止的事只要出現一次，這條禁止就被推翻。
- Definition: Never Occurs is one form of prohibition: it states that something does not happen; if the prohibited thing happens even once, the prohibition is refuted.

<a id="c02"></a>
## public 不被保證 / Not Guaranteed

- 定義：不被保證是禁止的一種形式：它不說某件事[不會發生](concepts.md#c01)，而是說框架不斷言它必然發生。具體情形中是否另有約束使它成立，框架也不斷言；要主張它必然發生，必須另外寫明所依賴的約束。它限制的是預測能有多少信心。
- Definition: Not Guaranteed is one form of prohibition: it does not state that something [never occurs](concepts.md#c01), but that the framework does not assert that it necessarily happens. Whether other constraints make it hold in a particular case, the framework does not assert either; to claim that it necessarily happens, one must state the constraints relied on. What it limits is how much confidence a prediction can have.
- 裁決：2026-09-28 作者裁決：稽核指出「沒有任何東西保證」過強；改為「框架推不出它必然發生」。
- 裁決：2026-09-29 作者裁決：稽核指出「框架推不出」是關於整個框架可推導性的後設命題，只看單一定義不足以證成；改為「框架不斷言它必然發生」，只談框架說了什麼，對照前提與定義即可核對。名稱不變，保留 ID。

<a id="c03"></a>
## private 宇宙 / Universe

- 定義：宇宙是有[坍塌](concepts.md#c07)的那一個世界。宇宙中不由過去決定的只有兩樣：[宇宙初態](concepts.md#c11)，與每一次[坍塌](concepts.md#c07)的結果；其他一切，包括每一次[寫入](concepts.md#c09)，都由到當時為止的[歷史](concepts.md#c10)決定。
- Definition: The universe is the one world in which there is [Collapse](concepts.md#c07). Only two things in the universe are not determined by the past: the [Initial State of the Universe](concepts.md#c11) and the outcome of each [collapse](concepts.md#c07); everything else, including every [Writing](concepts.md#c09), is determined by the [History](concepts.md#c10) up to that point.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的宇宙（c02）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的宇宙（c02），不再自行定義。

<a id="c04"></a>
## private 呈現 / Presentation

- 定義：呈現是[宇宙](concepts.md#c03)中的任何一部分。
- Definition: A presentation is any part of the [Universe](concepts.md#c03).
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的呈現（c04）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的呈現（c04），不再自行定義。

<a id="c05"></a>
## private 分支 / Branch

- 定義：分支是一次[坍塌](concepts.md#c07)可能得出的每一個結果。
- Definition: A branch is each possible outcome of a [Collapse](concepts.md#c07).
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的分支（c05）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的分支（c05），不再自行定義。

<a id="c06"></a>
## private 權重 / Weight

- 定義：權重是每個[分支](concepts.md#c05)的輕重。一次[坍塌](concepts.md#c07)的各[分支](concepts.md#c05)權重都不小於零，總和為一。一個[呈現](concepts.md#c04)的權重，是[宇宙](concepts.md#c03)的權重落在它身上的那一部分：把各[分支](concepts.md#c05)看成該[呈現](concepts.md#c04)的結果之後，同一個結果的權重相加。權重是[分支](concepts.md#c05)的輕重，不是任何[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)內部的參數。
- Definition: A weight is how heavy each [Branch](concepts.md#c05) is. The weights of the [branches](concepts.md#c05) of a [Collapse](concepts.md#c07) are all non-negative and sum to one. The weight of a [Presentation](concepts.md#c04) is the part of the weight of the [Universe](concepts.md#c03) that falls on it: once each [branch](concepts.md#c05) is read as an outcome for that [presentation](concepts.md#c04), the weights of [branches](concepts.md#c05) giving the same outcome are added together. A weight belongs to [branches](concepts.md#c05); it is not an internal parameter of any [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16).
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的權重（c06）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的權重（c06），不再自行定義。

<a id="c07"></a>
## private 坍塌 / Collapse

- 定義：坍塌是依[權重](concepts.md#c06)從各[分支](concepts.md#c05)中定出一個的事件。未被定出的[分支](concepts.md#c05)，不留在[宇宙](concepts.md#c03)中。給定到當時為止的[歷史](concepts.md#c10)，坍塌的結果依[權重](concepts.md#c06)定出，[歷史](concepts.md#c10)中沒有任何東西能比[權重](concepts.md#c06)多說一點。
- Definition: A collapse is the event in which one of the [Branches](concepts.md#c05) is settled according to the [Weights](concepts.md#c06). [Branches](concepts.md#c05) not settled do not remain in the [Universe](concepts.md#c03). Given the [History](concepts.md#c10) up to that point, the outcome of a collapse is settled according to the [weights](concepts.md#c06), and nothing in the [history](concepts.md#c10) says anything more than the [weights](concepts.md#c06).
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的坍塌（c08）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的坍塌（c08），不再自行定義。

<a id="c08"></a>
## private 時刻 / Moment

- 定義：時刻是一次[坍塌](concepts.md#c07)。[坍塌](concepts.md#c07)一次一次發生，排成一條全序；沒有[坍塌](concepts.md#c07)，就沒有時刻。一個[呈現](concepts.md#c04)的時刻，是[涉及](concepts.md#c14)它的那些[坍塌](concepts.md#c07)；它的下一時刻，是下一次[涉及](concepts.md#c14)它的[坍塌](concepts.md#c07)；兩次之間，對它沒有時刻。
- Definition: A moment is a [Collapse](concepts.md#c07). [Collapses](concepts.md#c07) happen one at a time and form a total order; without [collapse](concepts.md#c07) there is no moment. The moments of a [Presentation](concepts.md#c04) are the [collapses](concepts.md#c07) that [involve](concepts.md#c14) it; its next moment is the next [collapse](concepts.md#c07) that [involves](concepts.md#c14) it; between two of them, there is no moment for it.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的時刻（c09）不是同一概念：上游為 private，本篇在自己的前提下重新定義；本篇不引入觀察者。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的時刻（c09），不再自行定義。

<a id="c09"></a>
## private 寫入 / Writing

- 定義：寫入是每一次[坍塌](concepts.md#c07)之前，先加入[歷史](concepts.md#c10)的一筆內容。寫入的內容，完全由到當時為止的[歷史](concepts.md#c10)決定。[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)的每一次[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)，都在[歷史](concepts.md#c10)中留下一次寫入。
- Definition: A writing is the item added to [History](concepts.md#c10) before each [Collapse](concepts.md#c07). The content of a writing is entirely determined by the [history](concepts.md#c10) up to that point. Every [Distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) of a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) leaves a writing in [history](concepts.md#c10).
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的寫入（c29）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的寫入（c29），不再自行定義。

<a id="c10"></a>
## private 歷史 / History

- 定義：歷史始於[宇宙初態](concepts.md#c11)，其後是每一次[寫入](concepts.md#c09)與每一次[坍塌](concepts.md#c07)的結果，依[時刻](concepts.md#c08)排列。[宇宙](concepts.md#c03)只有一部歷史；歷史只往後追加，已經在其中的，不會離開。
- Definition: History begins with the [Initial State of the Universe](concepts.md#c11), followed by every [Writing](concepts.md#c09) and the outcome of every [Collapse](concepts.md#c07), arranged by [Moment](concepts.md#c08). The [universe](concepts.md#c03) has only one history; history is only ever appended to, and nothing already in it ever leaves.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的歷史（c13）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的歷史（c13），不再自行定義。

<a id="c11"></a>
## private 宇宙初態 / Initial State of the Universe

- 定義：宇宙初態是第一次[坍塌](concepts.md#c07)之前已經在那裡的一切，是[歷史](concepts.md#c10)的起點。內容不分析。
- Definition: The initial state of the universe is everything that is already there before the first [Collapse](concepts.md#c07); it is the starting point of [History](concepts.md#c10). Its content is not analyzed.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的宇宙初態（c14）不是同一概念：上游為 private，本篇在自己的前提下重新定義；本篇不引入形式層。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的宇宙初態（c14），不再自行定義。

<a id="c12"></a>
## private 生成函數 / Generating Function

- 定義：生成函數把到當時為止的[歷史](concepts.md#c10)，包括剛發生的那一次[寫入](concepts.md#c09)，對應到下一[時刻](concepts.md#c08)各[分支](concepts.md#c05)的[權重](concepts.md#c06)。它只取[歷史](concepts.md#c10)，只有一個，內部不分析。
- Definition: The generating function maps the [History](concepts.md#c10) up to that point, including the [Writing](concepts.md#c09) that has just taken place, to the [Weights](concepts.md#c06) of the [Branches](concepts.md#c05) of the next [Moment](concepts.md#c08). It takes nothing but [history](concepts.md#c10), there is only one of it, and its internals are not analyzed.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的生成函數（c15）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的生成函數（c15），不再自行定義。

<a id="c13"></a>
## private 到達 / Arrival

- 定義：一個[呈現](concepts.md#c04)收到的[刺激](concepts.md#c53)、[涉及](concepts.md#c14)它而實際結果沒有落在它身上的[坍塌](concepts.md#c07)，以及[宇宙初態](concepts.md#c11)，都成為它[歷史](concepts.md#c10)的一部分，這就是到達。也就是說，一個[呈現](concepts.md#c04)所處的物理條件，不論有沒有改變，都成為它的[歷史](concepts.md#c10)；一件可能發生在它身上、結果沒有發生的事，也算在內。
- Definition: The [Stimuli](concepts.md#c53) a [Presentation](concepts.md#c04) receives, the [Collapses](concepts.md#c07) that [involve](concepts.md#c14) it but whose actual outcome does not fall on it, and the [Initial State of the Universe](concepts.md#c11) all become part of its [History](concepts.md#c10); this is arrival. In other words, the physical conditions a [presentation](concepts.md#c04) is in, whether or not they change, all become its [history](concepts.md#c10); something that could have happened to it but did not also counts.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的到達（c10）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-29 改以刺激（c53）表述；上游渦的定義用到刺激，本篇依自己的前提重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的到達（c10），不再自行定義。

<a id="c14"></a>
## private 涉及 / Involvement

- 定義：一次[坍塌](concepts.md#c07)的結果以正的[權重](concepts.md#c06)落在一個[呈現](concepts.md#c04)身上，這次[坍塌](concepts.md#c07)就涉及它。
- Definition: A [Collapse](concepts.md#c07) involves a [Presentation](concepts.md#c04) when its outcome falls on that [presentation](concepts.md#c04) with positive [Weight](concepts.md#c06).
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的涉及（c11）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的涉及（c11），不再自行定義。

<a id="c15"></a>
## private 鎖死 / Lock-in

- 定義：鎖死是一個[呈現](concepts.md#c04)的下一[時刻](concepts.md#c08)只有一個[權重](concepts.md#c06)不為零的[分支](concepts.md#c05)的情形。鎖死是被[涉及](concepts.md#c14)了，卻只有一個結果；一個[呈現](concepts.md#c04)沒有[時刻](concepts.md#c08)的期間，不是鎖死。
- Definition: Lock-in is the situation in which the next [Moment](concepts.md#c08) of a [Presentation](concepts.md#c04) has only one [Branch](concepts.md#c05) of non-zero [Weight](concepts.md#c06). Lock-in means being [involved](concepts.md#c14) yet having only one outcome; a period in which a [presentation](concepts.md#c04) has no [moment](concepts.md#c08) is not lock-in.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的鎖死（c12）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的鎖死（c12），不再自行定義。

<a id="c16"></a>
## private 自己的那一段 / Own Segment

- 定義：一個[呈現](concepts.md#c04)自己的那一段，是它下一[時刻](concepts.md#c08)的[權重](concepts.md#c06)所依賴的那部分[歷史](concepts.md#c10)：[歷史](concepts.md#c10)中某一件若換成別的，影響沿著[歷史](concepts.md#c10)傳下來，會改變它下一[時刻](concepts.md#c08)[權重](concepts.md#c06)的分布，這一件就屬於它自己的那一段。那一段由依賴決定，不由任何[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)決定；它是同一部[歷史](concepts.md#c10)的一部分，不同[呈現](concepts.md#c04)自己的那一段可以重疊。
- Definition: The own segment of a [Presentation](concepts.md#c04) is the part of [History](concepts.md#c10) on which the [Weights](concepts.md#c06) of its next [Moment](concepts.md#c08) depend: an item of [history](concepts.md#c10) belongs to its own segment if replacing that item with something else would, with the influence passing down the [history](concepts.md#c10), change the distribution of its [weights](concepts.md#c06) for the next [moment](concepts.md#c08). The segment is determined by dependence, not by any [Distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18); it is part of the one [history](concepts.md#c10), and the own segments of different [presentations](concepts.md#c04) may overlap.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的自己的那一段（c17）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的自己的那一段（c17），不再自行定義。

<a id="c17"></a>
## private 個體 / Individual

- 定義：個體是被某個[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)出來的[呈現](concepts.md#c04)。同一段[宇宙](concepts.md#c03)，被[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)為一個個體或許多個個體，皆成立。
- Definition: An individual is a [Presentation](concepts.md#c04) marked off by some [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) through [Distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18). The same stretch of the [Universe](concepts.md#c03) may be [distinguished](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) as one individual or as many, and either holds.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的個體（c20）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的個體（c20），不再自行定義。

<a id="c18"></a>
## private 同一 / Identity

- 定義：同一是一種[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)：某一[時刻](concepts.md#c08)的[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)與另一[時刻](concepts.md#c08)的[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)是不是同一個，由作出[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)的[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)決定。一個[呈現](concepts.md#c04)是不是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)，不是[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)；兩個[時刻](concepts.md#c08)的[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)是不是同一個，是[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)。
- Definition: Identity is a kind of [Distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18): whether a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at one [Moment](concepts.md#c08) and a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at another [moment](concepts.md#c08) are the same one is decided by the [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) that makes the [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18). Whether a [Presentation](concepts.md#c04) is a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) is not a [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18); whether the [wos](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at two [moments](concepts.md#c08) are the same one is.
- 狀態：已廢棄
- 裁決：2026-09-28 判定與上游《渦動力學》的同一（c21）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的同一（c21），不再自行定義。

<a id="c19"></a>
## public 對他者認識的不可完備性 / Incompleteness of Knowing Others

- 定義：對他者認識的不可完備性是這個結論：沒有任何[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)能保證自己[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)完了另一個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)。[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)只能用已經[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)它的[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)來[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)對方；對方的訊息[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)它是一件事，它的[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)又是另一件，在兩者之間，對方的[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)仍可能增長，而對方不被[鎖死](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12)時，對方下一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)的結果不能事先推出（[黑箱律](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c25)）。此外，對方[自己的那一段](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17)中只[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)對方的部分，沒有任何東西保證也會[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)它；而它的[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)作為一次[寫入](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c29)，一旦成為對方[自己的那一段](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17)的一部分，也可能改變對方下一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)。
- Definition: The Incompleteness of Knowing Others is the conclusion that no [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) can guarantee that it has finished [distinguishing](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) another [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16). A self can distinguish the other only through the [History](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) that has [arrived](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at it; the other's information [arriving](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) is one event and the self's [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) is another, and between them the other's [history](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) may still grow, while, when the other is not [locked in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12), the outcome of the other's next [Moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) cannot be derived beforehand ([Law of the Black Box](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c25)). Moreover, nothing guarantees that the parts of the other's [Own Segment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17) that [arrive](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) only at the other also [arrive](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the self; and once the self's [distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18), as a [Writing](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c29), becomes part of the other's [own segment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17), it may change the other's [Weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) for its next [moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09).
- 裁決：2026-09-28 判定與上游《渦動力學》的他者分別不完（c32）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-28 修飾子定為 public：下游引用印象相關的禁止時需要它。
- 裁決：2026-09-28 名稱由「他者分別不完」改為「對他者認識的不可完備性」。
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c20"></a>
## private 粒度 / Granularity

- 定義：粒度是這個條件結論：若把同一段[宇宙](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c02)描述成較粗與較細的[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)，且較粗[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的結果投影可由較細[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的結果投影經某映射取得，則較粗[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)分布等於較細[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)分布經該映射推前所得。粒度只主張這種結果投影相容的描述之間一致，不主張任意兩個結果投影必然相容。
- Definition: Granularity is the conditional conclusion that, when the same stretch of the [Universe](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c02) is described by a coarser and a finer [Presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04), and the outcome projection of the coarser [presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) can be obtained from the outcome projection of the finer [presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) by a mapping, the [Weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) distribution of the coarser [presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) equals the pushforward, by that mapping, of the [weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) distribution of the finer [presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04). Granularity asserts consistency only between descriptions whose outcome projections are compatible in this sense; it does not assert that arbitrary outcome projections must be compatible.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。
- 裁決：2026-10-05 稽核指出原定義省略論文形式式中的「結果投影相容」前件，將條件命題擴大為無條件結論；本次收窄回形式式，不新增「所有重切皆投影相容」的公設，保留 ID。

<a id="c21"></a>
## public 時鐘時間 / Clock Time

- 定義：時鐘時間是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)以時鐘量到的時間，依物理，不形式化。它與[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)不同：[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)只是[坍塌](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c08)的次序，任何[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)都量不到。
- Definition: Clock time is the time a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) measures with a clock; it follows physics and is not formalized. It differs from a [Moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09): [moments](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) are only the order of [Collapses](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c08), which no [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) can measure.
- 裁決：2026-09-30 修飾子改為 public：公開工具「速度」用到它。
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c22"></a>
## public 重切 / Recutting

- 定義：重切是改變[切法](concepts.md#c23)：把一個部分切成許多部分，或把許多部分合成一個。「結論經得起重切」只適用於結果投影相容的重切：同一段[宇宙](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c02)被描述成較粗與較細的[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)時，較粗[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的結果投影可由較細[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的結果投影經某映射取得。[迴路](concepts.md#c31)、[知見障](concepts.md#c35)、[耦合](concepts.md#c36)、[太極](concepts.md#c39)與[更新](concepts.md#c42)所得的結論，在這些結果投影相容的重切下都必須不變；[印象](concepts.md#c45)不受此要求。在這些相容重切之下，[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)與推論不變；[耦合](concepts.md#c36)、[太極](concepts.md#c39)與[印象](concepts.md#c45)在描述[迴路](concepts.md#c31)之間的關係時，若相關部分被合併，這些關係轉為內部結構，不再以關係的形式出現。任意兩個[切法](concepts.md#c23)之間是否滿足這項結果投影相容條件，不由此定義保證。
- Definition: Recutting is changing the [Cut](concepts.md#c23): cutting one part into many, or merging many parts into one. Saying that a conclusion survives recutting applies only to outcome-projection-compatible recuttings: when the same stretch of the [Universe](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c02) is described by a coarser and a finer [Presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04), the outcome projection of the coarser [presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) can be obtained from the outcome projection of the finer [presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) by a mapping. Conclusions derived from [Loop](concepts.md#c31), [Cognitive Obscuration](concepts.md#c35), [Coupling](concepts.md#c36), [Taiji](concepts.md#c39), and [Renewal](concepts.md#c42) must remain unchanged under such outcome-projection-compatible recuttings; [Impression](concepts.md#c45) is exempt from this requirement. Under such compatible recuttings, [Weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) and inferences do not change; when [Coupling](concepts.md#c36), [Taiji](concepts.md#c39), and [Impression](concepts.md#c45) describe relations between [Loops](concepts.md#c31), merging the relevant parts turns those relations into internal structure, so they no longer appear as relations. This definition does not guarantee that arbitrary pairs of [Cuts](concepts.md#c23) satisfy this outcome-projection compatibility condition.
- 裁決：2026-09-28 稽核指出定義漏了論文正文的例外；補上「除了印象以外」。
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。
- 裁決：2026-10-05 稽核指出「分析的結論」範圍大於正文的「每個工具的結論」；定義收窄回正文範圍，中英文同步修改。
- 裁決：2026-10-05 稽核指出「每個工具」的量化範圍必須回到正文才能判定，違反定義可獨立理解；改為直接列出[迴路](concepts.md#c31)、[知見障](concepts.md#c35)、[耦合](concepts.md#c36)、[更新](concepts.md#c42)，並明列[印象](concepts.md#c45)為例外；同時移除「描述迴路關係的工具」這個未定義集合用語。意義不變，中英文同步修改。
- 裁決：2026-10-05 稽核指出正文「每個工具的結論」亦包含[太極](concepts.md#c39)；補入[太極](concepts.md#c39)，與正文範圍同步，中英文定義一併修改。
- 裁決：2026-10-05 配合前述結果投影相容條件的裁決，重切下「權重與推論不變」只對結果投影相容的重切成立；不新增任意切法皆相容的公設。
- 裁決：2026-10-06 稽核指出定義中的「本篇所說／本篇不主張」構成論文語境指示；改為直接陳述「結論經得起重切」的適用條件，並以「此定義不保證」表達任意切法未必相容。適用範圍與結論不變，中英文同步修改。

<a id="c23"></a>
## public 切法 / Cut

- 定義：切法是分析者的一次[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)：把哪一段[宇宙](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c02)看作一個[迴路](concepts.md#c31)。敘述[迴路](concepts.md#c31)必須切出部分，所以切法無法避免，必須講明。
- Definition: A cut is a [Distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) made by the analyst: which stretch of the [Universe](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c02) is taken as one [Loop](concepts.md#c31). Describing a [loop](concepts.md#c31) requires cutting out parts, so a cut cannot be avoided and must be stated.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c24"></a>
## private 邊界 / Boundary

- 定義：邊界是一個範例的地理邊界與時間邊界。它是一次[切法](concepts.md#c23)，每個範例都必須先講明。
- Definition: The boundary is the geographical and temporal boundary of a worked example. It is a [Cut](concepts.md#c23), and every example must state it first.

<a id="c25"></a>
## public 節點 / Node

- 定義：節點是[切法](concepts.md#c23)下的一個[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)，是假名。節點有三類：[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)、[信號容器](concepts.md#c26)、[隨機源](concepts.md#c27)。
- Definition: A node is a [Presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) under a [Cut](concepts.md#c23); it is a provisional name. There are three kinds of node: the [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), the [Signal Container](concepts.md#c26), and the [Random Source](concepts.md#c27).
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c26"></a>
## public 信號容器 / Signal Container

- 定義：信號容器是這樣的[節點](concepts.md#c25)：它不是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)，從這一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)起每一步都被[鎖死](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12)。它通常持續存在，在許多[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)向[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)[輸出](concepts.md#c28)，但這不是條件。它的內容可以由[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)寫下，例如書、法條；也可以來自其他[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)，例如望遠鏡帶來的觀測。[知見障](concepts.md#c35)對它沒有內容；它也不能成為[印象](concepts.md#c45)的對象。
- Definition: A signal container is a [Node](concepts.md#c25) that is not a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) and, from this [moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) on, is [locked in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12) at every step. It usually persists and [outputs](concepts.md#c28) to [wos](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at many [Moments](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09), but this is not a condition. Its content may be written by [wos](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), as with books or statutes, or may come from other [Presentations](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04), as with observations brought by a telescope. [Cognitive Obscuration](concepts.md#c35) has no content for it; nor can it be the object of an [Impression](concepts.md#c45).
- 裁決：2026-09-28 作者裁決放寬定義：持續存在、向渦輸出不再是條件，使節點的分類窮盡。
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。
- 裁決：2026-10-05 上游《渦動力學》勘誤後，渦的第二個條件改為往後看；本條隨之改為從這一刻起往後看，否則從某刻起永遠被鎖死、而歷史仍全部參與的節點，三類都不屬於。定稿前修改，保留 ID。
- 裁決：2026-10-05 稽核指出定義中的「這一刻」指涉上游的時刻而未連結，補上連結。意義不變，保留 ID。

<a id="c27"></a>
## public 隨機源 / Random Source

- 定義：隨機源是這樣的[節點](concepts.md#c25)：它不是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)，但這一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)或之後至少還有一個[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)它不被[鎖死](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12)。例如擲出的骰子、放射性衰變的原子。它不被[鎖死](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12)時，只能談[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)；它也不能成為[印象](concepts.md#c45)的對象。
- Definition: A random source is a [Node](concepts.md#c25) that is not a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), but at this [moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) or later there is still at least one [Moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) at which it is not [locked in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12). Examples are a thrown die and a radioactively decaying atom. When it is not [locked in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12), only its [weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) can be discussed; nor can it be the object of an [Impression](concepts.md#c45).
- 裁決：2026-09-28 作者裁決新增，使節點的分類窮盡。
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。
- 裁決：2026-10-05 隨上游勘誤改為往後看，與信號容器一起使節點的分類仍然窮盡。「歷史並不都參與它的權重」是由渦的定義推出的後果，從定義移到論文正文（步驟十四）。定稿前修改，保留 ID。
- 裁決：2026-10-05 稽核指出定義中的「這一刻」指涉上游的時刻而未連結，補上連結。意義不變，保留 ID。

<a id="c28"></a>
## public 輸出 / Output

- 定義：輸出是一個[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的[坍塌](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c08)結果，以及它的[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)內容。
- Definition: The output of a [Presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) is the outcomes of its [Collapses](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c08), together with the content of its [Distinctions](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18).
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c29"></a>
## public 邊 / Edge

- 定義：邊是[節點](concepts.md#c25)之間有向的關係：一個[節點](concepts.md#c25)的[輸出](concepts.md#c28)，參與另一個[節點](concepts.md#c25)的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)。邊本身不帶大小：一條邊影響多大，取決於接收者的[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)，不是邊本身的屬性。
- Definition: An edge is a directed relation between [Nodes](concepts.md#c25): the [Output](concepts.md#c28) of one [node](concepts.md#c25) takes part in the [Weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) of another. An edge carries no magnitude of its own: how much an edge matters depends on the receiver's [History](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13), not on the edge.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c30"></a>
## private 時序 / Timing

- 定義：時序是[邊](concepts.md#c29)的先後，以及[迴路](concepts.md#c31)繞一圈要多久；[節點](concepts.md#c25)可以在[邊界](concepts.md#c24)內的某個時間出現或消失。
- Definition: Timing is the order in which [Edges](concepts.md#c29) occur and how long a [Loop](concepts.md#c31) takes to go round once; [Nodes](concepts.md#c25) may appear or disappear at some time within the [Boundary](concepts.md#c24).

<a id="c31"></a>
## public 迴路 / Loop

- 定義：迴路是這樣的結構：一個[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的[輸出](concepts.md#c28)，經由自己或其他[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)，又參與它自己的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)。迴路一律指在講明的[切法](concepts.md#c23)下的迴路；跨[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)持續存在是[不被保證](concepts.md#c02)的。
- Definition: A loop is a structure in which the [Output](concepts.md#c28) of a [Presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04), through itself or through other [presentations](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04), takes part again in its own [Weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06). A loop is always a loop under a stated [Cut](concepts.md#c23); persistence across [Moments](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) is [Not Guaranteed](concepts.md#c02).
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c32"></a>
## public 正回饋 / Positive Feedback

- 定義：正回饋是[迴路](concepts.md#c31)的一種作用：[迴路](concepts.md#c31)自己的一次[輸出](concepts.md#c28)繞回來，把[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)推向產生這次[輸出](concepts.md#c28)的那個方向。方向必須包含這次[輸出](concepts.md#c28)，由它錨定；方向劃得多粗，屬於[切法](concepts.md#c23)。
- Definition: Positive feedback is an action of a [Loop](concepts.md#c31): one of the loop's own [Outputs](concepts.md#c28) comes back round and pushes the [Weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) toward the direction that produced that [output](concepts.md#c28). The direction must contain that [output](concepts.md#c28), which anchors it; how coarsely the direction is drawn belongs to the [Cut](concepts.md#c23).
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c33"></a>
## public 負回饋 / Negative Feedback

- 定義：負回饋是[迴路](concepts.md#c31)的一種作用：[迴路](concepts.md#c31)自己的一次[輸出](concepts.md#c28)繞回來，把[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)推離產生這次[輸出](concepts.md#c28)的那個方向。方向必須包含這次[輸出](concepts.md#c28)，由它錨定；方向劃得多粗，屬於[切法](concepts.md#c23)。抵抗若來自另一個[迴路](concepts.md#c31)的[輸出](concepts.md#c28)經由[耦合](concepts.md#c36)壓過來，不是負回饋。
- Definition: Negative feedback is an action of a [Loop](concepts.md#c31): one of the loop's own [Outputs](concepts.md#c28) comes back round and pushes the [Weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) away from the direction that produced that [output](concepts.md#c28). The direction must contain that [output](concepts.md#c28), which anchors it; how coarsely the direction is drawn belongs to the [Cut](concepts.md#c23). Resistance that comes from the [output](concepts.md#c28) of another [loop](concepts.md#c31) pressing in through [Coupling](concepts.md#c36) is not negative feedback.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c34"></a>
## public 速度 / Loop Speed

- 定義：速度是[迴路](concepts.md#c31)繞一圈要多久，以[時鐘時間](concepts.md#c21)計，與[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)不同。
- Definition: Loop speed is how long a [Loop](concepts.md#c31) takes to go round once, measured in [Clock Time](concepts.md#c21), which differs from [Moments](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09).
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c35"></a>
## public 知見障 / Cognitive Obscuration

- 定義：知見障是這件事：一個[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)在下一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)各個結果的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)，總和為一，所以推高一個方向，必然壓低其餘方向的總和。一切學習都有代價，知與障是同一件事。知見障只保證此消彼長，不決定一筆[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)推高哪個方向、推高多少。
- Definition: Cognitive obscuration is the fact that the [Weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) of the outcomes of a [Presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) at its next [Moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) sum to one, so that pushing one direction up necessarily pushes the total of the other directions down. All learning has a cost; knowing and obscuration are one and the same. Cognitive obscuration guarantees only that one rises as the others fall; it does not decide which direction an [Arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) pushes up, or by how much.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c36"></a>
## public 耦合 / Coupling

- 定義：耦合是這樣的情形：兩個或多個[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的[自己的那一段](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17)，在一個講明的[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)上重疊。參與耦合的可以是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)，也可以是由許多[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)構成的[迴路](concepts.md#c31)。重疊有兩種方式：同一件事[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)它們，例如同一個[輸出](concepts.md#c28)，或[宇宙初態](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c14)；或者它們的[輸出](concepts.md#c28)互相[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)。耦合總是相對於一個講明的[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)而言。
- Definition: Coupling is the situation in which the [Own Segments](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17) of two or more [Presentations](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) overlap in a stated [Arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10). What takes part in coupling may be [Wos](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), or [Loops](concepts.md#c31) made of many [presentations](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04). The overlap takes one of two forms: the same thing [arrives](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at all of them, such as the same [Output](concepts.md#c28) or the [Initial State of the Universe](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c14), or their [outputs](concepts.md#c28) [arrive](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at one another. Coupling is always relative to a stated [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10).
- 裁決：2026-09-29 稽核指出宇宙初態不是輸出，與「任何兩個渦都在宇宙初態上耦合」衝突；第一種方式由「同一個輸出」放寬為「同一件事」，兩種方式仍窮盡。定稿前修改，保留 ID。
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c37"></a>
## public 趨同 / Convergence

- 定義：趨同是[耦合](concepts.md#c36)之下的一種走向：在一個講明的維度上，參與者[輸出](concepts.md#c28)的模式之間的距離縮短。
- Definition: Convergence is one direction of development under [Coupling](concepts.md#c36): along a stated dimension, the distance between the patterns of the participants' [Outputs](concepts.md#c28) shrinks.

<a id="c38"></a>
## public 趨異 / Divergence

- 定義：趨異是[耦合](concepts.md#c36)之下的一種走向：在一個講明的維度上，參與者[輸出](concepts.md#c28)的模式之間的距離拉長。
- Definition: Divergence is one direction of development under [Coupling](concepts.md#c36): along a stated dimension, the distance between the patterns of the participants' [Outputs](concepts.md#c28) grows.

<a id="c39"></a>
## public 太極 / Taiji

- 定義：太極是這樣的結構：兩個方向相反的[正回饋](concepts.md#c32)[迴路](concepts.md#c31)互相[耦合](concepts.md#c36)，在[耦合](concepts.md#c36)中[趨異](concepts.md#c38)，形成動態平衡。每一方的[輸出](concepts.md#c28)[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)另一方，都強化了另一方自己的方向，所以任何一方壯大，都在餵養對方。太極發生在[迴路](concepts.md#c31)與[迴路](concepts.md#c31)之間，不要求每個參與者都是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)；「方向相反」必須講明是在哪一個維度上判斷。
- Definition: Taiji is a structure in which two [Loops](concepts.md#c31) of [Positive Feedback](concepts.md#c32) pointing in opposite directions are [coupled](concepts.md#c36) with each other and show [Divergence](concepts.md#c38) within that [coupling](concepts.md#c36), forming a dynamic balance. Each side's [Output](concepts.md#c28), [arriving](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) at the other, strengthens the other's own direction, so whichever side grows is feeding its opponent. Taiji takes place between [loops](concepts.md#c31), and does not require every participant to be a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16); for "opposite directions", the dimension on which this is judged must be stated.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c40"></a>
## public 一方退出 / Withdrawal of One Side

- 定義：一方退出是[太極](concepts.md#c39)結束的一種方式：其中一方的[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)大多[退場](concepts.md#c43)，或者它不再自我強化。
- Definition: Withdrawal of one side is one way a [Taiji](concepts.md#c39) ends: most of the [Wos](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) on one side [exit](concepts.md#c43), or that side no longer reinforces itself.

<a id="c41"></a>
## public 合流 / Merging

- 定義：合流是[太極](concepts.md#c39)結束的一種方式：[耦合](concepts.md#c36)的走向由[趨異](concepts.md#c38)轉為[趨同](concepts.md#c37)，兩個[迴路](concepts.md#c31)合成一個。在[重切](concepts.md#c22)之下，這也可以看作兩個[迴路](concepts.md#c31)被切成了一個。
- Definition: Merging is one way a [Taiji](concepts.md#c39) ends: the [Coupling](concepts.md#c36) turns from [Divergence](concepts.md#c38) to [Convergence](concepts.md#c37), and the two [Loops](concepts.md#c31) become one. Under [Recutting](concepts.md#c22), this can also be seen as the two [loops](concepts.md#c31) being cut as one.

<a id="c42"></a>
## public 更新 / Renewal

- 定義：更新是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)的進出：有些[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)[退場](concepts.md#c43)，有些[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)[進場](concepts.md#c44)。
- Definition: Renewal is the going and coming of [Wos](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16): some [wos](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) [exit](concepts.md#c43) and some [Presentations](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) [enter](concepts.md#c44).
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。
- 裁決：2026-10-05 退場、進場改為只連結引用，不在本條重述其定義。意義不變，保留 ID。
- 裁決：2026-10-05 刪去「新渦的自己的那一段和舊渦的重疊，但不可能完全相同」：重疊由任何兩個渦都在宇宙初態上重疊推出，不完全相同由不可復原律推出，兩者都是後果，不是定義，留在論文正文。定稿前修改，保留 ID。
- 裁決：2026-10-05 更正前一裁決：「不可復原律」不能推出進場者與舊渦的[自己的那一段](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17)必然不完全相同。不同[個體](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c20)可以共享共同前史，分開後可依新的[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)與[寫入](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c29)而分岔，之後也可以合併；是否為[同一](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c21)屬於[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)，不由更新或歷史重疊本身決定。正文與形式化同步刪除「不可能完全相同」。定稿前修改，保留 ID。

<a id="c43"></a>
## public 退場 / Exit

- 定義：退場是一個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)不再是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)：它在某一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)，在它的下一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)不是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)。
- Definition: Exit is a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) ceasing to be a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16): it is a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at one [Moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) and is not a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at its next [moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09).
- 裁決：2026-09-28 名稱由「出」改為「退場」。
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。
- 裁決：2026-10-05 退場就是渦不再是渦：刪去多加的條件「參與消失」；刪去「自己的那一段仍在歷史裡」「留下的輸出仍可透過信號容器到達後來的渦」兩句，它們是後果，不是定義，留在論文正文。定稿前修改，保留 ID。
- 裁決：2026-10-05 補上以它自己相鄰的兩個時刻比較：若凍結期間沒有坍塌涉及它，凍結期間就沒有它的時刻，解凍後若仍是渦，就不算退場。

<a id="c44"></a>
## public 進場 / Entry

- 定義：進場是一個[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)開始成為[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)：它在某一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)不是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)，在它的下一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)；或者在它的第一個[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)就是[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)。
- Definition: Entry is a [Presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) beginning to be a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16): it is not a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at one [Moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) and is a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at its next [moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09), or it is already a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) at its first [moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09).
- 裁決：2026-09-28 名稱由「入」改為「進場」。
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。
- 裁決：2026-10-05 比照退場：進場就是一個呈現開始成為渦；「自己的那一段和舊渦的重疊」屬於更新的內容，從本條刪去，留在論文正文。定稿前修改，保留 ID。
- 裁決：2026-10-05 比照退場，以它自己相鄰的兩個時刻比較；第一個時刻就是渦，也算進場。

<a id="c45"></a>
## public 印象 / Impression

- 定義：印象是一個[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)對一個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)的[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)，也就是它對那個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)的估計。對象可以是另一個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)，也可以是這個[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)自己，此時是[自我印象](concepts.md#c46)。對[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)的一切統計估計，包括史料的統計，都屬於印象。印象的材料有三個來源：對方過去的[輸出](concepts.md#c28)；[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)自己和對方重疊的[那一段](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17)；對方所在的[耦合](concepts.md#c36)，例如角色、制度、信仰。
- Definition: An impression is a [Distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) made by a [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) of a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), that is, its estimate of that [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)'s [Weights](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06). The object may be another [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16) or the [self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19) itself, in which case it is a [Self-Impression](concepts.md#c46). Every statistical estimate about a [wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), including statistics of historical records, is an impression. The material of an impression has three sources: the other's past [Outputs](concepts.md#c28); the part of the self's [own segment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c17) that overlaps with the other's; and the [Couplings](concepts.md#c36) the other is in, such as roles, institutions and beliefs.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c46"></a>
## public 自我印象 / Self-Impression

- 定義：自我印象是對象為自己的[印象](concepts.md#c45)：一個[我](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)對自己的[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)。
- Definition: A self-impression is an [Impression](concepts.md#c45) whose object is oneself: a [Self](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c19)'s [Distinction](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) of itself.

<a id="c47"></a>
## public 份量 / Impact

- 定義：份量是一筆[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)能移動多少[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)，是[生成函數](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c15)內部的事。份量不能相加：一筆[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)的效果，取決於它之前的[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)。
- Definition: The impact of an [Arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) is how much [Weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) it can move; it is a matter internal to the [Generating Function](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c15). Impacts do not add up: the effect of an [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) depends on the [History](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) before it.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c48"></a>
## public 頻率 / Frequency

- 定義：頻率是[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)多常發生。它是應用時必須寫明的[份量](concepts.md#c47)假定之一。
- Definition: Frequency is how often an [Arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) happens. It is one of the assumptions about [Impact](concepts.md#c47) that an application must state.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c49"></a>
## public 歧異度 / Discrepancy

- 定義：歧異度是一筆[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)和接收者過去收到的東西差多遠。它是相對於接收者的[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)而言的：同一個[到達](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10)，落在不同的[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)上，歧異度可以完全不同。它是應用時必須寫明的[份量](concepts.md#c47)假定之一。
- Definition: Discrepancy is how far an [Arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10) lies from what the receiver has received before. It is relative to the receiver's [History](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13): the same [arrival](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c10), falling on different [Wos](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16), can have entirely different discrepancy. It is one of the assumptions about [Impact](concepts.md#c47) that an application must state.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c50"></a>
## public 應用假定 / Application Assumption

- 定義：應用假定是這個假定：在生物學與社會科學的層次上，一個[呈現](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04)的下一[時刻](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09)若不能由那個層次可得的[歷史](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13)唯一推出，就假定它不被[鎖死](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12)。它不是定理。
- Definition: The application assumption is the assumption that, at the levels of biology and the social sciences, if the next [moment](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c09) of a [Presentation](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c04) cannot be uniquely derived from the [History](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c13) available at that level, it is assumed not to be [locked in](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c12). It is not a theorem.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。
- 裁決：2026-10-05 稽核指出定義中的「下一刻」指涉上游的時刻而未連結，補上連結。意義不變，保留 ID。

<a id="c51"></a>
## public 經驗禁止 / Empirical Prohibition

- 定義：經驗禁止是從案例中提煉出的必要條件：缺少某個條件時，某個結局[不會發生](concepts.md#c01)。它不是由框架推出，而是由跨案例的校準得來，必須標明，並可以被新的案例修改。它只寫必要條件，不寫充分條件：條件都滿足時，也只能說那個結局的[權重](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06)變重。
- Definition: An empirical prohibition is a necessary condition distilled from cases: without a certain condition, a certain outcome [Never Occurs](concepts.md#c01). It is not derived from the framework but obtained by calibration across cases, so it must be marked as such and may be revised by new cases. It states only necessary conditions, never sufficient ones: even when all the conditions are met, one can only say that the [Weight](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c06) of that outcome has become heavier.
- 裁決：2026-09-30 定義中的底層詞改連《渦動力學》的公開條目，意義不變，保留 ID。

<a id="c52"></a>
## public 持平 / Stasis

- 定義：持平是[耦合](concepts.md#c36)之下的一種走向：在一個講明的維度上，參與者[輸出](concepts.md#c28)的模式之間的距離不變。
- Definition: Stasis is one direction of development under [Coupling](concepts.md#c36): along a stated dimension, the distance between the patterns of the participants' [Outputs](concepts.md#c28) stays the same.
- 裁決：2026-09-28 作者裁決新增，使耦合之下的走向窮盡。

<a id="c53"></a>
## private 刺激 / Stimulus

- 定義：一次[寫入](concepts.md#c09)中落在一個[呈現](concepts.md#c04)身上的部分，以及落在它身上的[坍塌](concepts.md#c07)結果，都是它收到的刺激。也就是說，刺激是這個[呈現](concepts.md#c04)所處的物理條件的改變，不論來自外界還是它自己；自己或別人作出的[分別](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18)，落在它身上時，也算在內。
- Definition: The part of a [Writing](concepts.md#c09) that falls on a [Presentation](concepts.md#c04), and the outcome of a [Collapse](concepts.md#c07) that falls on it, are both stimuli it receives. In other words, a stimulus is a change in the physical conditions the [presentation](concepts.md#c04) is in, whether it comes from outside or from the presentation itself; [distinctions](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c18) made by itself or by others are included when they fall on it.
- 狀態：已廢棄
- 裁決：2026-09-29 判定與上游《渦動力學》的刺激（c30）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的刺激（c30），不再自行定義。

<a id="c54"></a>
## private 復原 / Restoration

- 定義：復原是讓一個[渦](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)[自己的那一段](concepts.md#c16)，整段再發生一次。也就是說，它的一切物理狀態，時間逆流，回到過去的某一個[時刻](concepts.md#c08)。
- Definition: Restoration is making the whole of a [Wo](../paper_37b3643a_dc40_4463_8b36_4f4594acd986/concepts.md#c16)'s [Own Segment](concepts.md#c16) happen once again. In other words, all of its physical states flow back in time to some past [Moment](concepts.md#c08).
- 狀態：已廢棄
- 裁決：2026-09-29 判定與上游《渦動力學》的復原（c27）不是同一概念：上游為 private，本篇在自己的前提下重新定義。
- 裁決：2026-09-30 廢棄：本篇直接引用《渦動力學》的復原（c27），不再自行定義。

## 不登記

- 對世界的禁止：2026-09-28 小節標題，其下說明句只是導讀，不是概念。
- 對說法的禁止：2026-09-28 小節標題，其下說明句只是導讀，不是概念。
- 方法上禁止：2026-09-28 對說法的禁止的形式標籤，指的是同一批禁止，不另立。
- 推出的不可能：2026-09-28 正文未另加定義，只用來與「沒出現過」對照。
- 禁止：2026-09-28 一般用語，正文未作為術語定義。
- 四條律：2026-09-28 流變律、黑箱律、損耗律、不可復原律的總稱，各律為上游概念。
- 具名推論與前提：2026-09-28 歷史不失去任何東西、對方可能因此改變、權重的形狀可以重複、知見障的出路，皆為論證中的具名命題，不是概念。
- 範例中的條件：2026-09-28 容器、持續的高歧異到達、舊一方缺少對等的到達、更新（條件四），皆為經驗禁止的實例，是命題，不是概念。
- 目的：2026-09-28 作者裁決不登記：明文第 6 條描述的是目的的特質，不是定義；目的不供引用。
- 一般用語：2026-09-28 經驗假定、史實、校準、高維、嵌套、維度、模式，正文未作為術語定義。
- 形式段名稱：2026-09-28 次序、分支集合、寫入規則、寫入後歷史、坍塌結果、歷史唯一、結果投影、未落在其上、推前、位置、替換、替換值、分布、實際值、時刻集合、分別內容、相互耦合、前一時刻，只出現在形式段，屬形式化階段的記號。

## 待決項

無
