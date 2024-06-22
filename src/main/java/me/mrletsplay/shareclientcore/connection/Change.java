package me.mrletsplay.shareclientcore.connection;

import me.mrletsplay.shareclientcore.document.Char;

public record Change(ChangeType type, Char character) {}
