# 渦動力學 Wo_Dynamics

一套公理化的理論框架：以同一組基本詞描述物理、生物、AI 與社會，並據此標出任何智能體在認識上的結構性極限。

An axiomatic framework that describes physics, biology, AI and society with one set of foundational terms, and uses it to mark the structural limits of what any agent can know.

資料集 / Dataset：[mthreetw/Wo-Dynamics-WD](https://huggingface.co/datasets/mthreetw/Wo-Dynamics-WD)（由本倉庫自動同步 / synced automatically from this repository）。

## 這套論文集要做什麼 / What this collection sets out to do

從一個最小的本體論出發：一個宇宙、一部只往後追加的歷史，加上依權重選出結果的坍塌。由此立下四條鐵律，再用同一套底層，一路往上描述能被歷史改變的個體、個體之間的互動、生產，以及「我們」、邊界、身分這類社會構件。

Starting from a minimal ontology — one universe, one history that only grows by appending, and collapses that select outcomes by weight — the framework lays down four iron laws, then builds upward on the same foundation: entities whose history shapes their next step, interaction between them, production, and social constructs such as "we", boundaries and identities.

目標有三個：

It has three aims:

1. **統一底層 / One foundation.**
   物理、生物、AI、社會使用同一組基本詞來描述，不為每一層另立生成規則。
   Physics, biology, AI and society are described with the same small set of terms; no separate generative rule is added for any layer.
2. **標出結構性的認識極限 / Structural limits of knowing.**
   任何智能體，不論是人還是 AI，對他者、對自己、對未來，能知道什麼、不能知道什麼，以及為什麼這些限制不會隨工程進步而消失。
   What any agent, human or AI, can and cannot know about others, about itself and about the future, and why these limits do not disappear with better engineering.
3. **作為處理高維資料的理論 / A theory for handling high-dimensional data.**
   當研究對象是歷史依賴、不被鎖死的系統時，壓縮、預測、比對各自能保證什麼、不能保證什麼。應用範圍是生物學、AI 與社會科學。
   When the object of study is a history-dependent system that is not locked in, what compression, prediction and comparison can and cannot guarantee. Intended fields of application: biology, AI and the social sciences.

### 論文樹的層次 / Layers of the paper tree

依引用關係排列，上游在前。 / Listed in dependency order, upstream first.

| 層 Layer | 論文 Papers | 回答的問題 Question addressed |
|---|---|---|
| 本體 Ontology | 暗宇宙的本體論 The Ontology of the Dark Universe；渦動力學 Wo Dynamics | 世界由什麼構成，什麼東西的歷史會影響它的下一步。What the world consists of, and what kind of entity has a next step shaped by its history. |
| 工具與極限 Tools and limits | 四條律相關工具及範例 Tools and Examples Related to the Four Laws；三重幻覺與被迫的賭注 Triple Illusion and the Forced Wager | 四條律能推出什麼；一個「我」為什麼看不全來處、自己與未來。What the four laws imply; why a self cannot fully see its origin, itself, or its future. |
| 互動 Interaction | 互動動力學-共同外生背景下的二階網路 Interaction Dynamics - Second-Order Networks under a Common Exogenous Background；互動動力學-陌生人迴路中的專屬歷史變動與更新 Interaction Dynamics - Changes and Updates of Dedicated History in Stranger Loops | 兩個以上的個體怎麼互相影響，文化與默契怎麼出現。How two or more entities affect each other; how culture and tacit coordination arise. |
| 生物人 Biological humans | 生產動力學 Production Dynamics；生物人個體輸出傾向動力學 Dynamics of Output Tendency in the Biological Human Individual；生物人輸出格局動力學 Dynamics of Output Patterns among Biological Humans | 個人與群體的行為怎麼記錄、歸納，哪些推不出。How individual and group behavior is recorded and generalized, and what cannot be inferred. |
| 構件 Constructs | 邊界的幻覺構件動力學 The Dynamics of Boundary Illusory Constructs；幻覺構件動力學 Dynamics of Illusory Constructs | 「我們」、邊界、概念這類東西怎麼從單純的物理反應生成。How "we", boundaries and concepts arise from mere physical responses. |

目前完成的是基礎層，應用層尚未開始。

The current papers form the foundation layer. The application layer has not started.

## 倉庫的組織方式 / How the repository is organized

- 每篇論文有自己的資料夾 `src/main/java/paper_<UUID>/`，內含四個檔案：中文論文（正本）、英文翻譯、概念表 `concepts.md`、形式化文件 `formalization.md`。
  Each paper has its own folder, `src/main/java/paper_<UUID>/`, with four files: the Chinese paper (source of truth), its English translation, a concept table (`concepts.md`), and a formalization file (`formalization.md`).
- 論文之間**只經由概念**互相引用。正文中的每個概念都是指向某份概念表條目的連結；下游論文只能引用上游的 `public` 條目。
  Papers cite each other **only through concepts**. Every concept in a paper's text is a link to an entry in some concept table; downstream papers may only cite `public` entries of upstream papers.
- 概念的身分是「論文 UUID + 概念 ID」。論文定稿（commit）後 ID 凍結；廢棄的條目保留不刪，ID 永不重用。
  A concept's identity is `paper UUID + concept ID`. After a paper is committed its IDs are frozen; deprecated entries are kept, and IDs are never reused.
- 外部來源（書籍、網頁、資料）以存根記錄在 `src/main/java/ext/`。
  External sources (books, web pages, data) are recorded as stubs in `src/main/java/ext/`.
- `tools/ToolRunner.java` 檢查連結與格式、與上一次 commit 比對、產生形式化與稽核的輸入包，並匯出 `exports/` 中的資料集。
  `tools/ToolRunner.java` checks links and formats, compares against the last commit, builds the input bundles for formalization and audit, and exports the datasets in `exports/`.

```
src/main/java/
  paper_<UUID>/
    中文標題.md        論文（正本） / paper (source of truth)
    English Title.md   英文翻譯 / English translation
    concepts.md        概念表（對外介面） / concept table (public interface)
    formalization.md   形式化文件 / formalization (for consistency audit)
  ext/ext_<UUID>/
    concepts.md        外部存根 / external stub
  tools/
    ToolRunner.java    檢查、打包、匯出 / check, bundle, export
    NewPaper.java      建立新論文 / create a new paper
src/main/resources/    規範文件 / specification documents
exports/               資料集（由 ToolRunner 產生） / datasets (generated by ToolRunner)
hf/README.md           Hugging Face 資料集卡片 / Hugging Face dataset card
```

### 論文與資料夾 / Papers and folders

| 論文 Paper | 資料夾 Folder |
|---|---|
| 暗宇宙的本體論 The Ontology of the Dark Universe | `paper_54c0291d_05b3_431a_9f2a_1b8ec93c3c3f` |
| 渦動力學 Wo Dynamics | `paper_37b3643a_dc40_4463_8b36_4f4594acd986` |
| 四條律相關工具及範例 Tools and Examples Related to the Four Laws | `paper_b9df3b68_23ee_4839_965b_ed0e6b283ee6` |
| 三重幻覺與被迫的賭注 Triple Illusion and the Forced Wager | `paper_fb0addca_42b6_4884_aa18_438c695747ab` |
| 互動動力學-共同外生背景下的二階網路 Interaction Dynamics - Second-Order Networks under a Common Exogenous Background | `paper_ae79e938_76e1_4bf9_b2ab_995c03e16c9e` |
| 互動動力學-陌生人迴路中的專屬歷史變動與更新 Interaction Dynamics - Changes and Updates of Dedicated History in Stranger Loops | `paper_2ccbf179_58cf_4851_bd72_5fa886e4b2c3` |
| 生產動力學 Production Dynamics | `paper_96f10d75_23d0_441f_94c7_ab87ca28d284` |
| 生物人個體輸出傾向動力學 Dynamics of Output Tendency in the Biological Human Individual | `paper_81952268_47b1_4d8c_9605_270c42646693` |
| 生物人輸出格局動力學 Dynamics of Output Patterns among Biological Humans | `paper_d244789a_9261_4a0d_a98a_feb8db56b511` |
| 邊界的幻覺構件動力學 The Dynamics of Boundary Illusory Constructs | `paper_08383b0a_1167_4efb_9b89_dc8a71afd5ff` |
| 幻覺構件動力學 Dynamics of Illusory Constructs | `paper_ed71bef7_0f22_4443_bc1d_4d1ecfe81967` |

### 規範文件 / Specification documents

規範文件在 `src/main/resources/`，目前只有中文版。

The specification documents are in `src/main/resources/`, currently in Chinese only.

| 文件 Document | 內容 Contents |
|---|---|
| 群星規範-說明文件 | 為什麼這樣設計、整體流程（先讀這份）。Design rationale and overall workflow (start here). |
| 群星規範-論文手冊 | 撰寫論文與英文翻譯。Writing papers and English translations. |
| 群星規範-概念表手冊 | 產出概念表、回填連結。Producing concept tables and backfilling links. |
| 群星規範-形式化手冊 | 產出形式化文件。Producing formalization files. |
| 群星規範-稽核總表 | 全部約束的正本，也是稽核的指令。The authoritative list of all constraints; also the audit instructions. |
| 群星規範-工具規格 | ToolRunner 與 NewPaper 的規格。Specification of ToolRunner and NewPaper. |

## 授權 / License

MIT。作者：黃正宇 / Cheng Yu Huang（mthree.tw@gmail.com）。

MIT License. Author: 黃正宇 / Cheng Yu Huang (mthree.tw@gmail.com).

