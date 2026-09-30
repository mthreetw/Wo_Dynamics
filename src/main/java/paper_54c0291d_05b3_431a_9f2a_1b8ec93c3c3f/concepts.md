---
uuid: 54c0291d-05b3-431a-9f2a-1b8ec93c3c3f
last-modified: 2026-09-27T01:51:21
---

<a id="c01"></a>
## public 奇點 / Singularity

- 定義：奇點是[時刻](concepts.md#c04)開始之前的來源。奇點本身沒有[時刻](concepts.md#c04)，也沒有之前，不可被形式化。[初始時刻](concepts.md#c05)的狀態由奇點而來，其來由不被追問。
- Definition: The singularity is the source that precedes the beginning of [Moments](concepts.md#c04). The singularity itself has no moment and no before, and cannot be formalized. The state of the [Initial Moment](concepts.md#c05) comes from the singularity, and its origin is not questioned.
- 裁決：2026-09-23 判定登記為概念。
- 裁決：2026-09-26 定義不綁定本篇的初始信，改指初始時刻的狀態，使下游可在自己的前提下使用。

<a id="c02"></a>
## public 形式層 / Formal Layer

- 定義：形式層是這樣一個宇宙：[時刻](concepts.md#c04)已經開始，[刺激](concepts.md#c07)已經[出現](concepts.md#c08)，[信](concepts.md#c26)已經[累積](concepts.md#c30)，而其中沒有觀察者。形式層中只有明列的[給定](concepts.md#c03)，以及由[給定](concepts.md#c03)推出的東西。
- Definition: The formal layer is a universe in which [Moments](concepts.md#c04) have begun, [Stimuli](concepts.md#c07) have [occurred](concepts.md#c08), and [Belief](concepts.md#c26) has [accumulated](concepts.md#c30), but in which there is no observer. The formal layer contains nothing but the explicitly listed [Givens](concepts.md#c03) and what is derived from them.
- 裁決：2026-09-23 判定登記為概念。
- 裁決：2026-09-23 加入封閉原則：形式層中只有明列的給定，以及由給定推出的東西。範圍限定於形式層，觀察者出現後不再適用。

<a id="c03"></a>
## public 給定 / Given

- 定義：給定是[形式層](concepts.md#c02)無法自己產生、只能被承認而不能被推出的東西。
- Definition: A given is something that the [Formal Layer](concepts.md#c02) cannot produce by itself; it can only be acknowledged, not derived.
- 裁決：2026-09-23 判定登記為概念。

<a id="c04"></a>
## public 時刻 / Moment

- 定義：時刻是時間的索引。時刻以正整數編號：[初始時刻](concepts.md#c05)是第一個，每個時刻的[後繼](concepts.md#c06)是編號加一的那個時刻；每個時刻都有編號。
- Definition: A moment is an index of time. Moments are numbered by the positive integers: the [Initial Moment](concepts.md#c05) is the first, the [Successor](concepts.md#c06) of each moment is the moment whose number is one greater, and every moment has a number.

<a id="c05"></a>
## public 初始時刻 / Initial Moment

- 定義：初始時刻是編號為一的[時刻](concepts.md#c04)，也就是第一個[時刻](concepts.md#c04)。
- Definition: The initial moment is the [Moment](concepts.md#c04) numbered one, that is, the first moment.
- 裁決：2026-09-23 判定與時刻、後繼分別登記。
- 裁決：2026-09-26 刪除「不是任何時刻的後繼」：論文未陳述。

<a id="c06"></a>
## public 後繼 / Successor

- 定義：某個[時刻](concepts.md#c04)的後繼，是編號比它大一的[時刻](concepts.md#c04)，也就是它的下一個[時刻](concepts.md#c04)。
- Definition: The successor of a [Moment](concepts.md#c04) is the moment whose number is one greater, that is, its next moment.
- 裁決：2026-09-23 判定與時刻、初始時刻分別登記。
- 裁決：2026-09-26 刪除「除初始時刻外，每個時刻都是某個時刻的後繼」：論文未陳述。

<a id="c07"></a>
## public 刺激 / Stimulus

- 定義：刺激是每個[時刻](concepts.md#c04)隨之[出現](concepts.md#c08)的物理內容，例如光、聲音、重力波、熱輻射。刺激不是宇宙的持存部分，而是每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)新[出現](concepts.md#c08)的自由度。
- Definition: A stimulus is the physical content that [occurs](concepts.md#c08) with each [Moment](concepts.md#c04), such as light, sound, gravitational waves, or thermal radiation. A stimulus is not a persistent part of the universe; it is a degree of freedom newly appearing at each [Successor](concepts.md#c06) [moment](concepts.md#c04).

<a id="c08"></a>
## public 出現 / Occurrence

- 定義：出現是[刺激](concepts.md#c07)與[時刻](concepts.md#c04)之間的關係：某[刺激](concepts.md#c07)與某[時刻](concepts.md#c04)滿足出現，表示該[刺激](concepts.md#c07)在該[時刻](concepts.md#c04)出現。出現是[刺激](concepts.md#c07)與[時刻](concepts.md#c04)之笛卡兒積的一個子集，隨[刺激](concepts.md#c07)一起[給定](concepts.md#c03)。
- Definition: Occurrence is the relation between [Stimuli](concepts.md#c07) and [Moments](concepts.md#c04): a stimulus and a moment stand in the occurrence relation when that stimulus appears at that moment. Occurrence is a subset of the Cartesian product of stimuli and moments, and is [given](concepts.md#c03) together with the stimuli.

<a id="c09"></a>
## private 初始信 / Initial Belief

- 定義：初始信是[初始時刻](concepts.md#c05)的[全域態](concepts.md#c25)，為[記錄空間](concepts.md#c15)中的一個單位向量。它是[給定](concepts.md#c03)的，不經[寫入](concepts.md#c21)而得，來自[奇點](concepts.md#c01)。
- Definition: The initial belief is the [Global State](concepts.md#c25) at the [Initial Moment](concepts.md#c05), a unit vector in the [Record Space](concepts.md#c15). It is [given](concepts.md#c03), not obtained through [Writing](concepts.md#c21), and comes from the [Singularity](concepts.md#c01).
- 裁決：2026-09-23 判定與信分別登記。
- 裁決：2026-09-26 改為 private：定義綁定本篇的全域態與記錄空間。

<a id="c10"></a>
## public 切分 / Partition

- 定義：切分是把宇宙劃為兩部分的界線：保留的部分是[信](concepts.md#c26)，被積掉的其餘部分是[環境](concepts.md#c11)。切分只劃線，不決定任何結果，因此切分不是觀察。
- Definition: The partition is the line that divides the universe into two parts: the retained part is [Belief](concepts.md#c26), and the traced-out remainder is the [Environment](concepts.md#c11). The partition only draws a line and decides no outcome; therefore the partition is not an observation.

<a id="c11"></a>
## public 環境 / Environment

- 定義：環境是[切分](concepts.md#c10)之下宇宙中[信](concepts.md#c26)以外、被積掉的其餘部分。
- Definition: The environment is the remainder of the universe, apart from [Belief](concepts.md#c26), that is traced out under the [Partition](concepts.md#c10).
- 裁決：2026-09-26 刪除環境隨時刻增長、初始時刻沒有環境、所在空間為環境總空間等描述：屬本篇公設二的構造，使下游可在自己的前提下使用。

<a id="c12"></a>
## private 宇宙空間 / Universe Space

- 定義：宇宙空間是宇宙在某個[時刻](concepts.md#c04)所處的空間，等於該[時刻](concepts.md#c04)的[信空間](concepts.md#c13)與[環境總空間](concepts.md#c14)的張量積。
- Definition: The universe space is the space in which the universe lies at a given [Moment](concepts.md#c04); it equals the tensor product of the [Belief Space](concepts.md#c13) and the [Total Environment Space](concepts.md#c14) at that moment.
- 裁決：2026-09-23 判定登記，名稱由作者確認。

<a id="c13"></a>
## private 信空間 / Belief Space

- 定義：信空間是[信](concepts.md#c26)所在的空間。在[初始時刻](concepts.md#c05)，信空間是一份[記錄空間](concepts.md#c15)；每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)，信空間再接上一份[記錄空間](concepts.md#c15)。
- Definition: The belief space is the space in which [Belief](concepts.md#c26) lives. At the [Initial Moment](concepts.md#c05) the belief space is one copy of the [Record Space](concepts.md#c15); at each [Successor](concepts.md#c06) [moment](concepts.md#c04) one more copy of the record space is appended to it.
- 裁決：2026-09-23 判定登記，名稱由作者確認。

<a id="c14"></a>
## private 環境總空間 / Total Environment Space

- 定義：環境總空間是某個[時刻](concepts.md#c04)全部[環境](concepts.md#c11)所在的空間。在[初始時刻](concepts.md#c05)，它是空乘積，表示沒有[環境](concepts.md#c11)；每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)，它再接上一份[環境空間](concepts.md#c16)。
- Definition: The total environment space is the space in which the whole [Environment](concepts.md#c11) lives at a given [Moment](concepts.md#c04). At the [Initial Moment](concepts.md#c05) it is the empty product, meaning there is no environment; at each [Successor](concepts.md#c06) [moment](concepts.md#c04) one more copy of the [Environment Space](concepts.md#c16) is appended to it.
- 裁決：2026-09-23 判定與環境空間分別登記：環境空間是一份環境的空間，環境總空間是全部環境的空間。名稱由作者確認。

<a id="c15"></a>
## private 記錄空間 / Record Space

- 定義：記錄空間是一筆[記錄](concepts.md#c17)所在的空間。它的基底是[給定](concepts.md#c03)的，即[記錄基底](concepts.md#c18)。
- Definition: The record space is the space in which a single [Record](concepts.md#c17) lives. Its basis is [given](concepts.md#c03), namely the [Record Basis](concepts.md#c18).
- 裁決：2026-09-23 判定記錄、記錄空間、記錄基底分別登記。
- 裁決：2026-09-23 刪除「維度至少為二」的條件：論文中沒有任何推導使用它。

<a id="c16"></a>
## private 環境空間 / Environment Space

- 定義：環境空間是一份[環境](concepts.md#c11)所在的空間，是[給定](concepts.md#c03)的，大小固定。每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)新增的一份[環境](concepts.md#c11)，住在一份環境空間中。
- Definition: The environment space is the space in which one part of the [Environment](concepts.md#c11) lives; it is [given](concepts.md#c03) and of fixed size. The part of the environment added at each [Successor](concepts.md#c06) [moment](concepts.md#c04) lives in one copy of the environment space.
- 裁決：2026-09-23 判定與環境總空間分別登記。

<a id="c17"></a>
## public 記錄 / Record

- 定義：記錄是[信](concepts.md#c26)的組成單位。每個[時刻](concepts.md#c04)對應一筆記錄：[初始時刻](concepts.md#c05)的記錄是[給定](concepts.md#c03)的，每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)則經[寫入](concepts.md#c21)新增一筆。[信](concepts.md#c26)由這些記錄依[時刻](concepts.md#c04)順序排成。
- Definition: A record is the constituent unit of [Belief](concepts.md#c26). Each [Moment](concepts.md#c04) corresponds to one record: the record of the [Initial Moment](concepts.md#c05) is [given](concepts.md#c03), and at each [Successor](concepts.md#c06) [moment](concepts.md#c04) one more record is added through [Writing](concepts.md#c21). Belief consists of these records arranged in the order of moments.
- 裁決：2026-09-23 判定記錄、記錄空間、記錄基底分別登記。
- 裁決：2026-09-26 定義不綁定本篇的初始信與記錄空間，使下游可在自己的前提下使用。

<a id="c18"></a>
## private 記錄基底 / Record Basis

- 定義：記錄基底是[記錄空間](concepts.md#c15)的[給定](concepts.md#c03)基底，規定[記錄](concepts.md#c17)以什麼基底讀出。
- Definition: The record basis is the [given](concepts.md#c03) basis of the [Record Space](concepts.md#c15); it determines in which basis a [Record](concepts.md#c17) is read out.
- 裁決：2026-09-23 判定記錄、記錄空間、記錄基底分別登記。
- 裁決：2026-09-26 改為 private：定義綁定本篇的記錄空間。

<a id="c19"></a>
## private 編碼 / Encoding

- 定義：編碼是把每個[刺激束](concepts.md#c27)對應到[刺激空間](concepts.md#c20)中一個單位向量的[給定](concepts.md#c03)規則。任何[刺激束](concepts.md#c27)，包括含多個[刺激](concepts.md#c07)的[刺激束](concepts.md#c27)，都有編碼值，具體的值不指定；[空刺激束](concepts.md#c23)的編碼是[真空態](concepts.md#c24)。
- Definition: Encoding is the [given](concepts.md#c03) rule that assigns to each [Stimulus Bundle](concepts.md#c27) a unit vector in the [Stimulus Space](concepts.md#c20). Every stimulus bundle, including those containing several [Stimuli](concepts.md#c07), has an encoded value, whose specific value is not fixed; the encoding of the [Empty Stimulus Bundle](concepts.md#c23) is the [Vacuum State](concepts.md#c24).

<a id="c20"></a>
## private 刺激空間 / Stimulus Space

- 定義：刺激空間是[刺激束](concepts.md#c27)經[編碼](concepts.md#c19)後所在的向量空間，是[給定](concepts.md#c03)的。
- Definition: The stimulus space is the vector space in which [Stimulus Bundles](concepts.md#c27) lie after [Encoding](concepts.md#c19); it is [given](concepts.md#c03).

<a id="c21"></a>
## public 寫入 / Writing

- 定義：寫入是每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)發生的過程：該[時刻](concepts.md#c04)的[刺激束](concepts.md#c27)被轉成一筆新的[記錄](concepts.md#c17)與一份新的[環境](concepts.md#c11)，接在宇宙既有的態之後。寫入只碰新[出現](concepts.md#c08)的[刺激](concepts.md#c07)，從不改動已經存在的東西。[初始時刻](concepts.md#c05)不發生寫入。
- Definition: Writing is the process that takes place at each [Successor](concepts.md#c06) [moment](concepts.md#c04): the [Stimulus Bundle](concepts.md#c27) of that moment is turned into one new [Record](concepts.md#c17) and one new part of the [Environment](concepts.md#c11), appended after the existing state of the universe. Writing touches only newly [occurring](concepts.md#c08) [Stimuli](concepts.md#c07) and never alters what already exists. No writing takes place at the [Initial Moment](concepts.md#c05).
- 裁決：2026-09-23 判定寫入與寫入映射為兩個概念：寫入是過程，寫入映射是映射。
- 裁決：2026-09-23 定義不綁定本篇的全域態，改指宇宙既有的態，使下游可在自己的前提下使用。
- 裁決：2026-09-26 定義不綁定本篇的編碼與寫入映射，使下游可在自己的前提下使用。

<a id="c22"></a>
## private 寫入映射 / Writing Map

- 定義：寫入映射是一個固定的等距映射，把[刺激空間](concepts.md#c20)送到一份[記錄空間](concepts.md#c15)與一份[環境空間](concepts.md#c16)的張量積。它是[給定](concepts.md#c03)的，每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)使用同一個寫入映射；它從何而來，在[形式層](concepts.md#c02)中不可知。
- Definition: The writing map is a fixed isometry from the [Stimulus Space](concepts.md#c20) into the tensor product of one copy of the [Record Space](concepts.md#c15) and one copy of the [Environment Space](concepts.md#c16). It is [given](concepts.md#c03), and the same writing map is used at every [Successor](concepts.md#c06) [moment](concepts.md#c04); where it comes from is unknowable within the [Formal Layer](concepts.md#c02).
- 裁決：2026-09-23 判定寫入與寫入映射為兩個概念：寫入是過程，寫入映射是映射。

<a id="c23"></a>
## private 空刺激束 / Empty Stimulus Bundle

- 定義：空刺激束是不含任何[刺激](concepts.md#c07)的[刺激束](concepts.md#c27)，即空集。它的[編碼](concepts.md#c19)是[真空態](concepts.md#c24)。
- Definition: The empty stimulus bundle is the [Stimulus Bundle](concepts.md#c27) that contains no [Stimulus](concepts.md#c07), that is, the empty set. Its [Encoding](concepts.md#c19) is the [Vacuum State](concepts.md#c24).
- 裁決：2026-09-23 判定與刺激束分別登記。

<a id="c24"></a>
## private 真空態 / Vacuum State

- 定義：真空態是[空刺激束](concepts.md#c23)的[編碼](concepts.md#c19)，為[刺激空間](concepts.md#c20)中的一個單位向量。它是真實的態，不是零向量。
- Definition: The vacuum state is the [Encoding](concepts.md#c19) of the [Empty Stimulus Bundle](concepts.md#c23), a unit vector in the [Stimulus Space](concepts.md#c20). It is a genuine state, not the zero vector.
- 裁決：2026-09-23 判定與空記錄分別登記。

<a id="c25"></a>
## private 全域態 / Global State

- 定義：全域態是宇宙在某個[時刻](concepts.md#c04)的態，住在該[時刻](concepts.md#c04)的[宇宙空間](concepts.md#c12)中。[初始時刻](concepts.md#c05)的全域態是[初始信](concepts.md#c09)；每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)的全域態，是前一[時刻](concepts.md#c04)的全域態接上[寫入映射](concepts.md#c22)作用於該[時刻](concepts.md#c04)[刺激束](concepts.md#c27)之[編碼](concepts.md#c19)所得的向量。全域態只經歷[寫入](concepts.md#c21)：舊的部分不動，新的接在後面。
- Definition: The global state is the state of the universe at a given [Moment](concepts.md#c04), living in the [Universe Space](concepts.md#c12) of that moment. The global state at the [Initial Moment](concepts.md#c05) is the [Initial Belief](concepts.md#c09); the global state at each [Successor](concepts.md#c06) [moment](concepts.md#c04) is the previous global state with, appended to it, the vector obtained by applying the [Writing Map](concepts.md#c22) to the [Encoding](concepts.md#c19) of that moment's [Stimulus Bundle](concepts.md#c27). The global state undergoes only [Writing](concepts.md#c21): the old part does not move, and the new part is appended after it.

<a id="c26"></a>
## public 信 / Belief

- 定義：信是宇宙在某個[時刻](concepts.md#c04)的態，在[切分](concepts.md#c10)之下的約化態，即對全部[環境](concepts.md#c11)取偏跡所得的密度算子。信沒有主體，不是誰的信；信是[累積](concepts.md#c30)本身。
- Definition: Belief is the reduced state, under the [Partition](concepts.md#c10), of the state of the universe at a given [Moment](concepts.md#c04), that is, the density operator obtained by taking the partial trace over the entire [Environment](concepts.md#c11). Belief has no subject and is not anyone's belief; belief is [Accumulation](concepts.md#c30) itself.
- 裁決：2026-09-23 判定與初始信分別登記。
- 裁決：2026-09-23 定義不綁定本篇的全域態，改指宇宙在某時刻的態，使下游可在自己的前提下使用。
- 裁決：2026-09-26 刪除「住在信空間上」：定義不綁定本篇的信空間。

<a id="c27"></a>
## public 刺激束 / Stimulus Bundle

- 定義：刺激束是某個[時刻](concepts.md#c04)所有[出現](concepts.md#c08)的[刺激](concepts.md#c07)組成的集合。刺激束可以是空集。
- Definition: A stimulus bundle is the set of all [Stimuli](concepts.md#c07) that [occur](concepts.md#c08) at a given [Moment](concepts.md#c04). A stimulus bundle may be the empty set.
- 裁決：2026-09-30 刪除「此時為空刺激束」：public 定義只連 public；只刪命名，意義不變。

<a id="c28"></a>
## private 空記錄 / Empty Record

- 定義：空記錄是某個[後繼](concepts.md#c06)[時刻](concepts.md#c04)的[刺激束](concepts.md#c27)為空集時，由[真空態](concepts.md#c24)經[寫入](concepts.md#c21)產生的那一筆[記錄](concepts.md#c17)。空記錄是一筆真實的[記錄](concepts.md#c17)，不是零筆[記錄](concepts.md#c17)。
- Definition: An empty record is the [Record](concepts.md#c17) produced through [Writing](concepts.md#c21) from the [Vacuum State](concepts.md#c24) at a [Successor](concepts.md#c06) [moment](concepts.md#c04) whose [Stimulus Bundle](concepts.md#c27) is the empty set. An empty record is a genuine record, not zero records.
- 裁決：2026-09-23 判定與真空態分別登記；用字統一為「記錄」。
- 裁決：2026-09-26 改為 private：定義綁定本篇的真空態。

<a id="c29"></a>
## public 最新記錄 / Latest Record

- 定義：最新記錄是某個[後繼](concepts.md#c06)[時刻](concepts.md#c04)的[信](concepts.md#c26)中最後[寫入](concepts.md#c21)的那一筆[記錄](concepts.md#c17)，也就是該[時刻](concepts.md#c04)本身對應的[記錄](concepts.md#c17)。
- Definition: The latest record is the [Record](concepts.md#c17) last [written](concepts.md#c21) in the [Belief](concepts.md#c26) of a given [Successor](concepts.md#c06) [moment](concepts.md#c04), that is, the record that corresponds to that moment itself.
- 裁決：2026-09-23 判定與記錄分別登記。
- 裁決：2026-09-26 限定為後繼時刻：初始時刻的記錄不經寫入。

<a id="c30"></a>
## public 累積 / Accumulation

- 定義：累積是[信](concepts.md#c26)隨[時刻](concepts.md#c04)增長的方式：每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)，[信](concepts.md#c26)多一筆[記錄](concepts.md#c17)，而對[最新記錄](concepts.md#c29)取跡後所得的正是前一[時刻](concepts.md#c04)的[信](concepts.md#c26)；過去的[記錄](concepts.md#c17)不被改寫。[信](concepts.md#c26)的改變不是覆蓋，而是增長。
- Definition: Accumulation is the way [Belief](concepts.md#c26) grows with [Moments](concepts.md#c04): at each [Successor](concepts.md#c06) [moment](concepts.md#c04) belief gains one more [Record](concepts.md#c17), and taking the trace over the [Latest Record](concepts.md#c29) yields exactly the belief of the previous moment; past records are never rewritten. The change of belief is not overwriting but growth.
- 裁決：2026-09-23 判定論文中作為一般詞使用的「累積」與具名結論的累積為同一概念。

<a id="c31"></a>
## private 演化規則 / Evolution Rule

- 定義：演化規則是把某[時刻](concepts.md#c04)的[全域態](concepts.md#c25)送到下一[時刻](concepts.md#c04)[全域態](concepts.md#c25)的映射，由[寫入](concepts.md#c21)決定：先接上該[時刻](concepts.md#c04)[刺激束](concepts.md#c27)的[編碼](concepts.md#c19)，再以[寫入映射](concepts.md#c22)作用於新接上的部分。
- Definition: The evolution rule is the map that sends the [Global State](concepts.md#c25) at one [Moment](concepts.md#c04) to the global state at the next moment, determined by [Writing](concepts.md#c21): first the [Encoding](concepts.md#c19) of that moment's [Stimulus Bundle](concepts.md#c27) is appended, then the [Writing Map](concepts.md#c22) acts on the newly appended part.

<a id="c32"></a>
## public 選出 / Selection

- 定義：選出是指把某個[時刻](concepts.md#c04)的態送到下一個[時刻](concepts.md#c04)之態的映射，不是等距映射的情形。[信](concepts.md#c26)在某個基底下呈對角、看起來像經典的機率分布，本身不構成選出。
- Definition: Selection is the situation in which the map sending the state at one [Moment](concepts.md#c04) to the state at the next [moment](concepts.md#c04) is not an isometry. [Belief](concepts.md#c26) being diagonal in some basis, and so looking like a classical probability distribution, does not by itself constitute selection.
- 裁決：2026-09-23 改為通用定義，不綁定本篇的演化規則：任何演化都可代入檢查是否構成選出。
- 裁決：2026-09-30 「在記錄基底下」改為「在某個基底下」：public 定義只連 public；改的是擋誤讀的句子，選出本身的意義不變。

<a id="c33"></a>
## private 非坍縮 / Non-Collapse

- 定義：非坍縮是指在每個[後繼](concepts.md#c06)[時刻](concepts.md#c04)，[形式層](concepts.md#c02)中的[全域態](concepts.md#c25)都未經歷[選出](concepts.md#c32)。非坍縮不否認[信](concepts.md#c26)可以在[記錄基底](concepts.md#c18)下呈對角、看起來像經典的機率分布；它否認的是[選出](concepts.md#c32)。
- Definition: Non-collapse is the condition that at every [Successor](concepts.md#c06) [moment](concepts.md#c04) the [Global State](concepts.md#c25) in the [Formal Layer](concepts.md#c02) undergoes no [Selection](concepts.md#c32). Non-collapse does not deny that [Belief](concepts.md#c26) may be diagonal in the [Record Basis](concepts.md#c18) and look like a classical probability distribution; what it denies is selection.
- 裁決：2026-09-26 改為 private：定義綁定本篇的全域態。

<a id="c34"></a>
## private 非退化模型 / Non-Degenerate Model

- 定義：非退化模型是一個具體的例子，含兩個[刺激](concepts.md#c07)，即[刺激甲](concepts.md#c35)與[刺激乙](concepts.md#c36)；[記錄空間](concepts.md#c15)與[環境空間](concepts.md#c16)皆為二維，基底標為零與一；[刺激空間](concepts.md#c20)為三維，基底為[真空態](concepts.md#c24)、甲、乙。[初始信](concepts.md#c09)為[記錄](concepts.md#c17)零。[刺激乙](concepts.md#c36)在[第二時刻](concepts.md#c37)[出現](concepts.md#c08)，[刺激甲](concepts.md#c35)在[第三時刻](concepts.md#c38)[出現](concepts.md#c08)，前三個[時刻](concepts.md#c04)內其餘的[出現](concepts.md#c08)皆為假。[寫入映射](concepts.md#c22)把[真空態](concepts.md#c24)送到[記錄](concepts.md#c17)零與[環境](concepts.md#c11)一，把甲送到「[記錄](concepts.md#c17)零、[環境](concepts.md#c11)零」與「[記錄](concepts.md#c17)一、[環境](concepts.md#c11)一」的等權疊加，把乙送到[記錄](concepts.md#c17)一與[環境](concepts.md#c11)零。
- Definition: The non-degenerate model is a concrete example with two [Stimuli](concepts.md#c07), namely [Stimulus A](concepts.md#c35) and [Stimulus B](concepts.md#c36). The [Record Space](concepts.md#c15) and the [Environment Space](concepts.md#c16) are both two-dimensional, with basis vectors labelled zero and one; the [Stimulus Space](concepts.md#c20) is three-dimensional, with basis the [Vacuum State](concepts.md#c24), A, and B. The [Initial Belief](concepts.md#c09) is record zero. Stimulus B [occurs](concepts.md#c08) at the [Second Moment](concepts.md#c37) and Stimulus A at the [Third Moment](concepts.md#c38); all other occurrences within the first three [moments](concepts.md#c04) are false. The [Writing Map](concepts.md#c22) sends the vacuum state to [record](concepts.md#c17) zero with [environment](concepts.md#c11) one, sends A to the equal-weight superposition of "record zero, environment zero" and "record one, environment one", and sends B to record one with environment zero.

<a id="c35"></a>
## private 刺激甲 / Stimulus A

- 定義：刺激甲是[非退化模型](concepts.md#c34)中的兩個[刺激](concepts.md#c07)之一，在[第三時刻](concepts.md#c38)[出現](concepts.md#c08)；其[編碼](concepts.md#c19)是[刺激空間](concepts.md#c20)中標為甲的基底向量。
- Definition: Stimulus A is one of the two [Stimuli](concepts.md#c07) of the [Non-Degenerate Model](concepts.md#c34); it [occurs](concepts.md#c08) at the [Third Moment](concepts.md#c38), and its [Encoding](concepts.md#c19) is the basis vector of the [stimulus space](concepts.md#c20) labelled A.

<a id="c36"></a>
## private 刺激乙 / Stimulus B

- 定義：刺激乙是[非退化模型](concepts.md#c34)中的兩個[刺激](concepts.md#c07)之一，在[第二時刻](concepts.md#c37)[出現](concepts.md#c08)；其[編碼](concepts.md#c19)是[刺激空間](concepts.md#c20)中標為乙的基底向量。
- Definition: Stimulus B is one of the two [Stimuli](concepts.md#c07) of the [Non-Degenerate Model](concepts.md#c34); it [occurs](concepts.md#c08) at the [Second Moment](concepts.md#c37), and its [Encoding](concepts.md#c19) is the basis vector of the [stimulus space](concepts.md#c20) labelled B.

<a id="c37"></a>
## private 第二時刻 / Second Moment

- 定義：第二時刻是[初始時刻](concepts.md#c05)的[後繼](concepts.md#c06)，即編號為二的[時刻](concepts.md#c04)。
- Definition: The second moment is the [Successor](concepts.md#c06) of the [Initial Moment](concepts.md#c05), that is, the [Moment](concepts.md#c04) numbered two.

<a id="c38"></a>
## private 第三時刻 / Third Moment

- 定義：第三時刻是[第二時刻](concepts.md#c37)的[後繼](concepts.md#c06)，即編號為三的[時刻](concepts.md#c04)。
- Definition: The third moment is the [Successor](concepts.md#c06) of the [Second Moment](concepts.md#c37), that is, the [Moment](concepts.md#c04) numbered three.

## 不登記

- 觀察者：2026-09-23 論文未定義；依封閉原則，它不屬於形式層。
- 序列：2026-09-23 非獨立概念，僅為公設二的敘述用語。

## 待決項

無
