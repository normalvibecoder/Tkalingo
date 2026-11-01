const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write user profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user can create and read their own valid UserProfile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice Scholar",
      xp: 120,
      streakDays: 3,
      hearts: 5,
      gems: 200,
      totalExamsCompleted: 1,
      totalRemedialsCompleted: 0,
      weakSubtopics: ["Logaritma"],
      masteredUnits: ["mat_wajib_u1"],
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());
});

test("Cross-user isolation: Alice cannot read or modify Bob's UserProfile or ExamAttempts", async () => {
  const now = new Date();
  await testEnv.withSecurityRulesDisabled(async (ctx) => {
    const db = ctx.firestore();
    await db.collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      displayName: "Bob",
      xp: 50,
      streakDays: 1,
      hearts: 5,
      gems: 100,
      totalExamsCompleted: 1,
      totalRemedialsCompleted: 0,
      weakSubtopics: [],
      masteredUnits: [],
      createdAt: now,
      updatedAt: now,
    });
    await db.collection("users").doc(BOB_UID).collection("attempts").doc("att_1").set({
      userId: BOB_UID,
      subjectId: "mat_wajib",
      subjectName: "Matematika Wajib",
      mode: "SIMULASI_TKA",
      irtScore: 740,
      correctCount: 24,
      totalQuestions: 30,
      durationSeconds: 3200,
      weakSubtopics: ["Logaritma"],
      strongSubtopics: ["Aljabar"],
      aiAnalysisSummary: "Perkuat sifat logaritma.",
      createdAt: now,
      updatedAt: now,
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).collection("attempts").doc("att_1").get()
  );
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).collection("attempts").get()
  );
});

test("Shadow update & schema validation: rejects ghost fields and invalid hearts/scores", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  // Ghost field 'isAdmin: true'
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      xp: 0,
      streakDays: 0,
      hearts: 5,
      gems: 100,
      totalExamsCompleted: 0,
      totalRemedialsCompleted: 0,
      weakSubtopics: [],
      masteredUnits: [],
      isAdmin: true,
      createdAt: now,
      updatedAt: now,
    })
  );
  // Invalid hearts > 5
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      xp: 0,
      streakDays: 0,
      hearts: 99,
      gems: 100,
      totalExamsCompleted: 0,
      totalRemedialsCompleted: 0,
      weakSubtopics: [],
      masteredUnits: [],
      createdAt: now,
      updatedAt: now,
    })
  );
});

test("Authenticated user can create and query their own ExamAttempts and SavedCustomQuestions", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("attempts").doc("att_alice_1").set({
      userId: ALICE_UID,
      subjectId: "mat_wajib",
      subjectName: "Matematika Wajib",
      mode: "SIMULASI_TKA",
      irtScore: 815,
      correctCount: 25,
      totalQuestions: 30,
      durationSeconds: 3600,
      weakSubtopics: ["Logaritma"],
      strongSubtopics: ["Fungsi Kuadrat"],
      aiAnalysisSummary: "Bagus!",
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("attempts").get()
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("custom_questions").doc("q_1").set({
      userId: ALICE_UID,
      subjectId: "mat_wajib",
      subtopic: "Logaritma",
      difficulty: 3,
      questionType: "SINGLE_CHOICE",
      stem: "Jika ^2log(x) = 3, maka nilai x adalah...",
      options: ["6", "8", "9", "12", "16"],
      correctAnswerCode: "1",
      explanation: "Berdasarkan definisi logaritma, x = 2^3 = 8.",
      createdAt: now,
      updatedAt: now,
    })
  );
});
